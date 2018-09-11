package com.mybatis.pseudocode.mybatis.executor;


import com.mybatis.pseudocode.mybatis.cache.CacheKey;
import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.mapping.*;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.session.LocalCacheScope;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;
import com.mybatis.pseudocode.mybatis.transaction.Transaction;
import com.mybatis.pseudocode.mybatis.type.TypeHandlerRegistry;
import org.apache.ibatis.cache.impl.PerpetualCache;

import org.apache.ibatis.executor.ExecutionPlaceholder;
import org.apache.ibatis.executor.statement.StatementUtil;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BaseExecutor implements Executor
{
    private static final Log log = LogFactory.getLog(BaseExecutor.class);
    protected Transaction transaction;
    protected Executor wrapper;
    protected ConcurrentLinkedQueue<DeferredLoad> deferredLoads;
    protected PerpetualCache localCache;
    protected PerpetualCache localOutputParameterCache;
    protected Configuration configuration;
    protected int queryStack;
    private boolean closed;

    protected BaseExecutor(Configuration configuration, Transaction transaction)
    {
        this.transaction = transaction;
        this.deferredLoads = new ConcurrentLinkedQueue();
        this.localCache = new PerpetualCache("LocalCache");
        this.localOutputParameterCache = new PerpetualCache("LocalOutputParameterCache");
        this.closed = false;
        this.configuration = configuration;
        this.wrapper = this;
    }

    public Transaction getTransaction()
    {
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        return this.transaction;
    }

    public void close(boolean forceRollback)
    {
        try {
            try {
                rollback(forceRollback);

                if (this.transaction != null)
                    this.transaction.close();
            }
            finally
            {
                if (this.transaction != null) {
                    this.transaction.close();
                }

            }

            this.localCache = null;
            this.localOutputParameterCache = null;
            this.closed = true;
        }
        catch (SQLException e)
        {
            log.warn("Unexpected exception on closing transaction.  Cause: " + e);
        } finally {
            this.transaction = null;
            this.deferredLoads = null;
            this.localCache = null;
            this.localOutputParameterCache = null;
            this.closed = true;
        }
    }

    public boolean isClosed()
    {
        return this.closed;
    }

    public int update(MappedStatement ms, Object parameter) throws SQLException
    {
        //ErrorContext.instance().resource(ms.getResource()).activity("executing an update").object(ms.getId());
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        clearLocalCache();
        return doUpdate(ms, parameter);
    }

    public List<BatchResult> flushStatements() throws SQLException
    {
        return flushStatements(false);
    }

    public List<BatchResult> flushStatements(boolean isRollBack) throws SQLException {
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        return doFlushStatements(isRollBack);
    }

    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException
    {
        BoundSql boundSql = ms.getBoundSql(parameter);
        CacheKey key = createCacheKey(ms, parameter, rowBounds, boundSql);
        return query(ms, parameter, rowBounds, resultHandler, key, boundSql);
    }

    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds,
                             ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException
    {
        //ErrorContext.instance().resource(ms.getResource()).activity("executing a query").object(ms.getId());
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        if ((this.queryStack == 0) && (ms.isFlushCacheRequired())) {
            clearLocalCache();
        }
        List list =null;
        try
        {
            this.queryStack += 1;
            list = resultHandler == null ? (List)this.localCache.getObject(key) : null;
            if (list != null)
                handleLocallyCachedOutputParameters(ms, key, parameter, boundSql);
            else
                list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql);
        }
        finally {
            this.queryStack -= 1;
        }
        if (this.queryStack == 0) {
            for (DeferredLoad deferredLoad : this.deferredLoads) {
                deferredLoad.load();
            }

            this.deferredLoads.clear();
            if (this.configuration.getLocalCacheScope() == LocalCacheScope.STATEMENT)
            {
                clearLocalCache();
            }
        }
        return list;
    }

    public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException
    {
        BoundSql boundSql = ms.getBoundSql(parameter);
        return doQueryCursor(ms, parameter, rowBounds, boundSql);
    }

    public void deferLoad(MappedStatement ms, MetaObject resultObject, String property, CacheKey key, Class<?> targetType)
    {
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        DeferredLoad deferredLoad = new DeferredLoad(resultObject, property, key, this.localCache, this.configuration, targetType);
        if (deferredLoad.canLoad())
            deferredLoad.load();
        else
            this.deferredLoads.add(new DeferredLoad(resultObject, property, key, this.localCache, this.configuration, targetType));
    }

    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql)
    {
        if (this.closed) {
            throw new ExecutorException("Executor was closed.");
        }
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(ms.getId());
        cacheKey.update(Integer.valueOf(rowBounds.getOffset()));
        cacheKey.update(Integer.valueOf(rowBounds.getLimit()));
        cacheKey.update(boundSql.getSql());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        TypeHandlerRegistry typeHandlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();

        for (ParameterMapping parameterMapping : parameterMappings) {
            if (parameterMapping.getMode() != ParameterMode.OUT)
            {
                String propertyName = parameterMapping.getProperty();
                Object value;
                if (boundSql.hasAdditionalParameter(propertyName)) {
                    value = boundSql.getAdditionalParameter(propertyName);
                }
                else
                {
                    if (parameterObject == null) {
                        value = null;
                    }
                    else
                    {
                        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                            value = parameterObject;
                        } else {
                            //MetaObject metaObject = this.configuration.newMetaObject(parameterObject);
                            MetaObject metaObject =null;
                            value = metaObject.getValue(propertyName);
                        }
                    }
                }
                cacheKey.update(value);
            }
        }
        if (this.configuration.getEnvironment() != null)
        {
            cacheKey.update(this.configuration.getEnvironment().getId());
        }
        return cacheKey;
    }

    public boolean isCached(MappedStatement ms, CacheKey key)
    {
        return this.localCache.getObject(key) != null;
    }

    public void commit(boolean required) throws SQLException
    {
        if (this.closed) {
            throw new ExecutorException("Cannot commit, transaction is already closed");
        }
        clearLocalCache();
        flushStatements();
        if (required)
            this.transaction.commit();
    }

    public void rollback(boolean required)
            throws SQLException
    {
        if (!this.closed)
            try {
                clearLocalCache();
                flushStatements(true);

                if (required)
                    this.transaction.rollback();
            }
            finally
            {
                if (required)
                    this.transaction.rollback();
            }
    }

    public void clearLocalCache()
    {
        if (!this.closed) {
            this.localCache.clear();
            this.localOutputParameterCache.clear();
        }
    }

    protected abstract int doUpdate(MappedStatement paramMappedStatement, Object paramObject) throws SQLException;

    protected abstract List<BatchResult> doFlushStatements(boolean paramBoolean) throws SQLException;

    protected abstract <E> List<E> doQuery(MappedStatement paramMappedStatement, Object paramObject, RowBounds paramRowBounds,
                                           ResultHandler paramResultHandler, BoundSql paramBoundSql) throws SQLException;

    protected abstract <E> Cursor<E> doQueryCursor(MappedStatement paramMappedStatement, Object paramObject,
                                                   RowBounds paramRowBounds, BoundSql paramBoundSql) throws SQLException;

    protected void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException localSQLException) {
            }
        }
    }

    protected void applyTransactionTimeout(Statement statement) throws SQLException
    {
        StatementUtil.applyTransactionTimeout(statement, Integer.valueOf(statement.getQueryTimeout()), this.transaction.getTimeout());
    }

    private void handleLocallyCachedOutputParameters(MappedStatement ms, CacheKey key, Object parameter, BoundSql boundSql)
    {
        MetaObject metaCachedParameter;
        MetaObject metaParameter;
        if (ms.getStatementType() == StatementType.CALLABLE) {
            Object cachedParameter = this.localOutputParameterCache.getObject(key);
            if ((cachedParameter != null) && (parameter != null)) {
//                metaCachedParameter = this.configuration.newMetaObject(cachedParameter);
//                metaParameter = this.configuration.newMetaObject(parameter);
                metaCachedParameter=null;
                metaParameter=null;
                for (ParameterMapping parameterMapping : boundSql.getParameterMappings())
                    if (parameterMapping.getMode() != ParameterMode.IN) {
                        String parameterName = parameterMapping.getProperty();
                        Object cachedValue = metaCachedParameter.getValue(parameterName);
                        metaParameter.setValue(parameterName, cachedValue);
                    }
            }
        }
    }

    private <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter, RowBounds rowBounds,
                                          ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException
    {
        this.localCache.putObject(key, ExecutionPlaceholder.EXECUTION_PLACEHOLDER);
        List<E> list = null;
        try {
            list = doQuery(ms, parameter, rowBounds, resultHandler, boundSql);
        }
        finally
        {
            this.localCache.removeObject(key);
        }
        this.localCache.putObject(key, list);
        if (ms.getStatementType() == StatementType.CALLABLE) {
            this.localOutputParameterCache.putObject(key, parameter);
        }
        return list;
    }

    protected Connection getConnection(Log statementLog) throws SQLException {
        Connection connection = this.transaction.getConnection();
        if (statementLog.isDebugEnabled()) {
            //return ConnectionLogger.newInstance(connection, statementLog, this.queryStack);
            return connection;
        }
        return connection;
    }

    public void setExecutorWrapper(Executor wrapper)
    {
        this.wrapper = wrapper;
    }

    private static class DeferredLoad {
        private final MetaObject resultObject;
        private final String property;
        private final Class<?> targetType;
        private final CacheKey key;
        private final PerpetualCache localCache;
        private final ObjectFactory objectFactory;
        private final ResultExtractor resultExtractor;

        public DeferredLoad(MetaObject resultObject, String property, CacheKey key, PerpetualCache localCache, Configuration configuration, Class<?> targetType) {
            this.resultObject = resultObject;
            this.property = property;
            this.key = key;
            this.localCache = localCache;
            this.objectFactory = configuration.getObjectFactory();
            this.resultExtractor = new ResultExtractor(configuration, this.objectFactory);
            this.targetType = targetType;
        }

        public boolean canLoad() {
            return (this.localCache.getObject(this.key) != null) && (this.localCache.getObject(this.key) != ExecutionPlaceholder.EXECUTION_PLACEHOLDER);
        }

        public void load()
        {
            List list = (List)this.localCache.getObject(this.key);
            Object value = this.resultExtractor.extractObjectFromList(list, this.targetType);
            this.resultObject.setValue(this.property, value);
        }
    }
}
