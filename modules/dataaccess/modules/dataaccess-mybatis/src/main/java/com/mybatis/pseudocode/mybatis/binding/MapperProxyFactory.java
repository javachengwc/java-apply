package com.mybatis.pseudocode.mybatis.binding;


import com.mybatis.pseudocode.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//Mapper接口代理对象的工厂
public class MapperProxyFactory<T>
{
    //Mapper接口类
    private final Class<T> mapperInterface;
    //当前Mapper接口中所有的方法
    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap();

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return this.mapperInterface;
    }

    public Map<Method, MapperMethod> getMethodCache() {
        return this.methodCache;
    }

    //创建Mapper接口的代理对象
    protected T newInstance(MapperProxy<T> mapperProxy)
    {
        return (T) Proxy.newProxyInstance(this.mapperInterface.getClassLoader(), new Class[] { this.mapperInterface }, mapperProxy);
    }

    public T newInstance(SqlSession sqlSession) {
        MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession, this.mapperInterface, this.methodCache);
        return newInstance(mapperProxy);
    }
}
