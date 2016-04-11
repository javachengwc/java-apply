package com.shop.service;

/**
 * 基本的增删改查服务
 */
public interface IService<T> {

    public int add(T t);

    public void delete(T t);

    public int update(T t);

    public T getById(T t);
}