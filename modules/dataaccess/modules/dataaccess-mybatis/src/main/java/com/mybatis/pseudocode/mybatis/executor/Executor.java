package com.mybatis.pseudocode.mybatis.executor;

import com.mybatis.pseudocode.mybatis.cache.CacheKey;
import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.mapping.BoundSql;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;
import com.mybatis.pseudocode.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

//myBatis执行器，是myBatis调度的核心，负责sql语句的生成和查询缓存的维护
public abstract interface Executor
{
    public static final ResultHandler NO_RESULT_HANDLER = null;

    public abstract int update(MappedStatement mappedStatement, Object object) throws SQLException;

    public abstract <E> List<E> query(MappedStatement mappedStatement, Object object, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException;

    public abstract <E> List<E> query(MappedStatement mappedStatement, Object object, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException;

    public abstract <E> Cursor<E> queryCursor(MappedStatement mappedStatement, Object object, RowBounds rowBounds) throws SQLException;

    public abstract List<BatchResult> flushStatements() throws SQLException;

    public abstract void commit(boolean paramBoolean) throws SQLException;

    public abstract void rollback(boolean paramBoolean) throws SQLException;

    //创建缓存key
    public abstract CacheKey createCacheKey(MappedStatement mappedStatement, Object object, RowBounds rowBounds, BoundSql boundSql);

    //是否缓存
    public abstract boolean isCached(MappedStatement mappedStatement, CacheKey cacheKey);

    public abstract void clearLocalCache();

    public abstract Transaction getTransaction();

    public abstract void close(boolean paramBoolean);

    public abstract boolean isClosed();

    public abstract void setExecutorWrapper(Executor executor);

    //延迟加载
    //public abstract void deferLoad(MappedStatement mappedStatement, MetaObject metaObject, String param, CacheKey cacheKey, Class<?> paramClass);
}
