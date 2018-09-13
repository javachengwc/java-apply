package com.mybatis.pseudocode.mybatisspring;

import com.mybatis.pseudocode.mybatis.exceptions.PersistenceException;
import com.mybatis.pseudocode.mybatis.mapping.Environment;
import com.mybatis.pseudocode.mybatis.session.ExecutorType;
import com.mybatis.pseudocode.mybatis.session.SqlSession;
import com.mybatis.pseudocode.mybatis.session.SqlSessionFactory;
import com.mybatis.pseudocode.mybatisspring.transaction.SpringManagedTransactionFactory;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public final class SqlSessionUtils
{
    private static final Log LOGGER = LogFactory.getLog(SqlSessionUtils.class);
    private static final String NO_EXECUTOR_TYPE_SPECIFIED = "No ExecutorType specified";
    private static final String NO_SQL_SESSION_FACTORY_SPECIFIED = "No SqlSessionFactory specified";
    private static final String NO_SQL_SESSION_SPECIFIED = "No SqlSession specified";

    public static SqlSession getSqlSession(SqlSessionFactory sessionFactory)
    {
        ExecutorType executorType = sessionFactory.getConfiguration().getDefaultExecutorType();
        return getSqlSession(sessionFactory, executorType, null);
    }

    public static SqlSession getSqlSession(SqlSessionFactory sessionFactory, ExecutorType executorType,
                                           PersistenceExceptionTranslator exceptionTranslator)
    {
        SqlSessionHolder holder = (SqlSessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);

        SqlSession session = sessionHolder(executorType, holder);
        if (session != null) {
            return session;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating a new SqlSession");
        }

        session = sessionFactory.openSession(executorType);

        registerSessionHolder(sessionFactory, executorType, exceptionTranslator, session);

        return session;
    }

    private static void registerSessionHolder(SqlSessionFactory sessionFactory, ExecutorType executorType,
                                              PersistenceExceptionTranslator exceptionTranslator, SqlSession session)
    {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            Environment environment = sessionFactory.getConfiguration().getEnvironment();

            if ((environment.getTransactionFactory() instanceof SpringManagedTransactionFactory)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Registering transaction synchronization for SqlSession [" + session + "]");
                }
                SqlSessionHolder holder = new SqlSessionHolder(session, executorType, exceptionTranslator);
                TransactionSynchronizationManager.bindResource(sessionFactory, holder);
                TransactionSynchronizationManager.registerSynchronization(new SqlSessionSynchronization(holder, sessionFactory));
                holder.setSynchronizedWithTransaction(true);
                holder.requested();
            }
            else if (TransactionSynchronizationManager.getResource(environment.getDataSource()) == null) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("SqlSession [" + session + "] was not registered for synchronization because DataSource is not transactional");
            }
            else {
                throw new TransientDataAccessResourceException("SqlSessionFactory must be using a SpringManagedTransactionFactory in order to use Spring transaction synchronization");
            }

        }
        else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SqlSession [" + session + "] was not registered for synchronization because synchronization is not active");
        }
    }

    private static SqlSession sessionHolder(ExecutorType executorType, SqlSessionHolder holder)
    {
        SqlSession session = null;
        if ((holder != null) && (holder.isSynchronizedWithTransaction())) {
            if (holder.getExecutorType() != executorType) {
                throw new TransientDataAccessResourceException("Cannot change the ExecutorType when there is an existing transaction");
            }

            holder.requested();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fetched SqlSession [" + holder.getSqlSession() + "] from current transaction");
            }

            session = holder.getSqlSession();
        }
        return session;
    }

    public static void closeSqlSession(SqlSession session, SqlSessionFactory sessionFactory)
    {
        SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        if ((holder != null) && (holder.getSqlSession() == session)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Releasing transactional SqlSession [" + session + "]");
            }
            holder.released();
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Closing non transactional SqlSession [" + session + "]");
            }
            session.close();
        }
    }

    public static boolean isSqlSessionTransactional(SqlSession session, SqlSessionFactory sessionFactory)
    {
        SqlSessionHolder holder = (SqlSessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
        return (holder != null) && (holder.getSqlSession() == session);
    }

    private static final class SqlSessionSynchronization extends TransactionSynchronizationAdapter
    {
        private final SqlSessionHolder holder;
        private final SqlSessionFactory sessionFactory;
        private boolean holderActive = true;

        public SqlSessionSynchronization(SqlSessionHolder holder, SqlSessionFactory sessionFactory) {

            this.holder = holder;
            this.sessionFactory = sessionFactory;
        }

        public int getOrder()
        {
            return 999;
        }

        public void suspend()
        {
            if (this.holderActive) {
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization suspending SqlSession [" + this.holder.getSqlSession() + "]");
                }
                TransactionSynchronizationManager.unbindResource(this.sessionFactory);
            }
        }

        public void resume()
        {
            if (this.holderActive) {
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization resuming SqlSession [" + this.holder.getSqlSession() + "]");
                }
                TransactionSynchronizationManager.bindResource(this.sessionFactory, this.holder);
            }
        }

        public void beforeCommit(boolean readOnly)
        {
            if (TransactionSynchronizationManager.isActualTransactionActive())
                try {
                    if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                        SqlSessionUtils.LOGGER.debug("Transaction synchronization committing SqlSession [" + this.holder.getSqlSession() + "]");
                    }
                    this.holder.getSqlSession().commit();
                } catch (PersistenceException p) {
                    if (this.holder.getPersistenceExceptionTranslator() != null)
                    {
                        DataAccessException translated = this.holder
                                .getPersistenceExceptionTranslator()
                                .translateExceptionIfPossible(p);

                        if (translated != null) {
                            throw translated;
                        }
                    }
                    throw p;
                }
        }

        public void beforeCompletion()
        {
            if (!this.holder.isOpen()) {
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization deregistering SqlSession [" + this.holder.getSqlSession() + "]");
                }
                TransactionSynchronizationManager.unbindResource(this.sessionFactory);
                this.holderActive = false;
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization closing SqlSession [" + this.holder.getSqlSession() + "]");
                }
                this.holder.getSqlSession().close();
            }
        }

        public void afterCompletion(int status)
        {
            if (this.holderActive)
            {
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization deregistering SqlSession [" + this.holder.getSqlSession() + "]");
                }
                TransactionSynchronizationManager.unbindResourceIfPossible(this.sessionFactory);
                this.holderActive = false;
                if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
                    SqlSessionUtils.LOGGER.debug("Transaction synchronization closing SqlSession [" + this.holder.getSqlSession() + "]");
                }
                this.holder.getSqlSession().close();
            }
            this.holder.reset();
        }
    }
}
