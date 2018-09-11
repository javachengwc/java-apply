package com.mybatis.pseudocode.mybatis.executor;

import com.mybatis.pseudocode.mybatis.cache.Cache;
import com.mybatis.pseudocode.mybatis.cache.CacheKey;
import com.mybatis.pseudocode.mybatis.cache.TransactionalCacheManager;
import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.mapping.*;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;
import com.mybatis.pseudocode.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

public class CachingExecutor implements Executor
{
    //委派的执行器(真正的执行器)
    private final Executor delegate;

    private final TransactionalCacheManager tcm = new TransactionalCacheManager();

    public CachingExecutor(Executor delegate) {
        this.delegate = delegate;
        delegate.setExecutorWrapper(this);
    }

    public Transaction getTransaction()
    {
        return this.delegate.getTransaction();
    }

    public void close(boolean forceRollback)
    {
        try
        {
            if (forceRollback)
                this.tcm.rollback();
            else {
                this.tcm.commit();
            }

            this.delegate.close(forceRollback);
        } finally {
            this.delegate.close(forceRollback);
        }
    }

    public boolean isClosed()
    {
        return this.delegate.isClosed();
    }

    public int update(MappedStatement ms, Object parameterObject) throws SQLException
    {
        flushCacheIfRequired(ms);
        return this.delegate.update(ms, parameterObject);
    }

    public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException
    {
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
        return query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
    }

    public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException
    {
        flushCacheIfRequired(ms);
        return this.delegate.queryCursor(ms, parameter, rowBounds);
    }

    public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds,
                             ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException
    {
        Cache cache = ms.getCache();
        if (cache != null) {
            flushCacheIfRequired(ms);
            if ((ms.isUseCache()) && (resultHandler == null)) {
                ensureNoOutParams(ms, boundSql);

                List list = (List)this.tcm.getObject(cache, key);
                if (list == null) {
                    //delegate真实的执行器是SimplyExecutor
                    list = this.delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
                    this.tcm.putObject(cache, key, list);
                }
                return list;
            }
        }
        return this.delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
    }

    public List<BatchResult> flushStatements() throws SQLException
    {
        return this.delegate.flushStatements();
    }

    public void commit(boolean required) throws SQLException
    {
        this.delegate.commit(required);
        this.tcm.commit();
    }

    public void rollback(boolean required) throws SQLException
    {
        try {
            this.delegate.rollback(required);

            if (required)
                this.tcm.rollback();
        }
        finally
        {
            if (required)
                this.tcm.rollback();
        }
    }

    private void ensureNoOutParams(MappedStatement ms, BoundSql boundSql)
    {
        if (ms.getStatementType() == StatementType.CALLABLE)
            for (ParameterMapping parameterMapping : boundSql.getParameterMappings())
                if (parameterMapping.getMode() != ParameterMode.IN)
                    throw new ExecutorException("Caching stored procedures with OUT params is not supported.  Please configure useCache=false in " + ms.getId() + " statement.");
    }

    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql)
    {
        return this.delegate.createCacheKey(ms, parameterObject, rowBounds, boundSql);
    }

    public boolean isCached(MappedStatement ms, CacheKey key)
    {
        return this.delegate.isCached(ms, key);
    }
//
//    public void deferLoad(MappedStatement ms, MetaObject resultObject, String property, CacheKey key, Class<?> targetType)
//    {
//        this.delegate.deferLoad(ms, resultObject, property, key, targetType);
//    }

    public void clearLocalCache()
    {
        this.delegate.clearLocalCache();
    }

    private void flushCacheIfRequired(MappedStatement ms) {
        Cache cache = ms.getCache();
        if ((cache != null) && (ms.isFlushCacheRequired()))
            this.tcm.clear(cache);
    }

    public void setExecutorWrapper(Executor executor)
    {
        throw new UnsupportedOperationException("This method should not be called");
    }
}
