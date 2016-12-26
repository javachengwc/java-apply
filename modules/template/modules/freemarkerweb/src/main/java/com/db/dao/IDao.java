package com.db.dao;

/**
 * 数据访问接口类
 */
public interface IDao<T> {

    public int add(T t);

    public void delete(T t);

    public int update(T t);

    public T getById(T t);
}
