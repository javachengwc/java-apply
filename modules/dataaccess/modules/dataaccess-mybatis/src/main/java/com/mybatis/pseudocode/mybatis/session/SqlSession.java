package com.mybatis.pseudocode.mybatis.session;

import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.executor.BatchResult;

import java.io.Closeable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

//所有操作数据库的api
public abstract interface SqlSession extends Closeable
{
    public abstract <T> T selectOne(String param);

    public abstract <T> T selectOne(String param, Object object);

    public abstract <E> List<E> selectList(String param);

    public abstract <E> List<E> selectList(String param, Object object);

    public abstract <E> List<E> selectList(String param, Object object, RowBounds rowBounds);

    public abstract <K, V> Map<K, V> selectMap(String param1, String param2);

    public abstract <K, V> Map<K, V> selectMap(String param1, Object object, String param2);

    public abstract <K, V> Map<K, V> selectMap(String param1, Object object, String param2, RowBounds rowBounds);

    public abstract <T> Cursor<T> selectCursor(String param);

    public abstract <T> Cursor<T> selectCursor(String param, Object object);

    public abstract <T> Cursor<T> selectCursor(String param, Object object, RowBounds rowBounds);

    public abstract void select(String param, Object object, ResultHandler resultHandler);

    public abstract void select(String param, ResultHandler resultHandler);

    public abstract void select(String param, Object object, RowBounds rowBounds, ResultHandler resultHandler);

    public abstract int insert(String param);

    public abstract int insert(String param, Object object);

    public abstract int update(String param);

    public abstract int update(String param, Object object);

    public abstract int delete(String param);

    public abstract int delete(String param, Object object);

    public abstract void commit();

    public abstract void commit(boolean paramBoolean);

    public abstract void rollback();

    public abstract void rollback(boolean paramBoolean);

    public abstract List<BatchResult> flushStatements();

    public abstract void close();

    public abstract void clearCache();

    public abstract Configuration getConfiguration();

    public abstract <T> T getMapper(Class<T> paramClass);

    public abstract Connection getConnection();
}
