package com.mybatis.pseudocode.mybatis.session;

import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.executor.BatchResult;

import java.io.Closeable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

//所有操作数据库的顶层api,表示和数据库交互的会话，完成必要数据库增删改查功能
public abstract interface SqlSession extends Closeable
{
    public abstract <T> T selectOne(String statmentId);

    public abstract <T> T selectOne(String statmentId, Object object);

    public abstract <E> List<E> selectList(String statmentId);

    public abstract <E> List<E> selectList(String statmentId, Object object);

    /**
     * @param statmentId   查询statment的标记id,就是Mapper.xml文件中<select>节点中的id属性值
     * @param parameter       设置sql中的参数
     * @param rowBounds    查询的范围
     */
    public abstract <E> List<E> selectList(String statmentId, Object parameter, RowBounds rowBounds);

    public abstract <K, V> Map<K, V> selectMap(String param1, String param2);

    public abstract <K, V> Map<K, V> selectMap(String param1, Object object, String param2);

    public abstract <K, V> Map<K, V> selectMap(String param1, Object object, String param2, RowBounds rowBounds);

    public abstract <T> Cursor<T> selectCursor(String param);

    public abstract <T> Cursor<T> selectCursor(String param, Object object);

    public abstract <T> Cursor<T> selectCursor(String param, Object object, RowBounds rowBounds);

    public abstract void select(String param, Object object, ResultHandler resultHandler);

    public abstract void select(String param, ResultHandler resultHandler);

    //resultHandler是查询结果的后置处理器，语句的查询结果直接交由它处理，就不返回给调用方了
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

    //获取type这个Mapper接口类的代理对象
    public abstract <T> T getMapper(Class<T> type);

    public abstract Connection getConnection();
}
