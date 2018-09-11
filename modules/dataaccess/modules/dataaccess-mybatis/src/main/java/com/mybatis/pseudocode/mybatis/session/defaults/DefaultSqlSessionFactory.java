package com.mybatis.pseudocode.mybatis.session.defaults;

import com.mybatis.pseudocode.mybatis.executor.Executor;
import com.mybatis.pseudocode.mybatis.mapping.Environment;
import com.mybatis.pseudocode.mybatis.session.*;
import com.mybatis.pseudocode.mybatis.transaction.Transaction;
import com.mybatis.pseudocode.mybatis.transaction.TransactionFactory;
import org.apache.ibatis.exceptions.ExceptionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DefaultSqlSessionFactory implements SqlSessionFactory
{
    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public SqlSession openSession()
    {
        return openSessionFromDataSource(this.configuration.getDefaultExecutorType(), null, false);
    }

    public SqlSession openSession(boolean autoCommit)
    {
        return openSessionFromDataSource(this.configuration.getDefaultExecutorType(), null, autoCommit);
    }

    public SqlSession openSession(ExecutorType execType)
    {
        return openSessionFromDataSource(execType, null, false);
    }

    public SqlSession openSession(TransactionIsolationLevel level)
    {
        return openSessionFromDataSource(this.configuration.getDefaultExecutorType(), level, false);
    }

    public SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level)
    {
        return openSessionFromDataSource(execType, level, false);
    }

    public SqlSession openSession(ExecutorType execType, boolean autoCommit)
    {
        return openSessionFromDataSource(execType, null, autoCommit);
    }

    public SqlSession openSession(Connection connection)
    {
        return openSessionFromConnection(this.configuration.getDefaultExecutorType(), connection);
    }

    public SqlSession openSession(ExecutorType execType, Connection connection)
    {
        return openSessionFromConnection(execType, connection);
    }

    public Configuration getConfiguration()
    {
        return this.configuration;
    }

    private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
        Transaction tx = null;
        try {
            Environment environment = this.configuration.getEnvironment();
            TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
            tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
            Executor executor = this.configuration.newExecutor(tx, execType);
            DefaultSqlSession localDefaultSqlSession = new DefaultSqlSession(this.configuration, executor, autoCommit);
            return localDefaultSqlSession;
        } catch (Exception e) {
            closeTransaction(tx);
            throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
        } finally {
            //ErrorContext.instance().reset();
        }
    }
    private SqlSession openSessionFromConnection(ExecutorType execType, Connection connection) {
        try {
            boolean autoCommit=true;
            try {
                autoCommit = connection.getAutoCommit();
            }
            catch (SQLException e)
            {
            }
            Environment environment = this.configuration.getEnvironment();
            TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
            Transaction tx = transactionFactory.newTransaction(connection);
            Executor executor = this.configuration.newExecutor(tx, execType);
            DefaultSqlSession localDefaultSqlSession = new DefaultSqlSession(this.configuration, executor, autoCommit);
            return localDefaultSqlSession;
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
        } finally {
            //ErrorContext.instance().reset();
        }
    }

    private TransactionFactory getTransactionFactoryFromEnvironment(Environment environment) {
        if ((environment == null) || (environment.getTransactionFactory() == null)) {
            //return new ManagedTransactionFactory();
            return null;
        }
        return environment.getTransactionFactory();
    }

    private void closeTransaction(Transaction tx) {
        if (tx != null)
            try {
                tx.close();
            }
            catch (SQLException localSQLException)
            {
            }
    }
}
