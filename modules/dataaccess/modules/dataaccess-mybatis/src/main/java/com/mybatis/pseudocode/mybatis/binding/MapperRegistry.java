package com.mybatis.pseudocode.mybatis.binding;


import com.mybatis.pseudocode.mybatis.builder.annotation.MapperAnnotationBuilder;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.session.SqlSession;

import java.util.*;

//Mapper接口的注册中心
public class MapperRegistry
{
    private final Configuration config;

    //key就是Mapper接口类，balue为该Mapper接口代理对象的工厂
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap();

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    //获取type这个Mapper接口类的代理对象
    public <T> T getMapper(Class<T> type, SqlSession sqlSession)
    {
        MapperProxyFactory mapperProxyFactory = (MapperProxyFactory)this.knownMappers.get(type);
        if (mapperProxyFactory == null)
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        try
        {
            return (T) mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public <T> boolean hasMapper(Class<T> type)
    {
        return this.knownMappers.containsKey(type);
    }

    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            if (hasMapper(type)) {
                //当type已经在mapper注册中心时，抛此异常
                throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
            }
            boolean loadCompleted = false;
            try {
                //创建MapperProxyFactory对象，并put进knownMappers中
                this.knownMappers.put(type, new MapperProxyFactory(type));
                MapperAnnotationBuilder parser = new MapperAnnotationBuilder(this.config, type);
                parser.parse();
                loadCompleted = true;
            } finally {
                if (!loadCompleted)
                    this.knownMappers.remove(type);
            }
        }
    }

    public Collection<Class<?>> getMappers()
    {
        return Collections.unmodifiableCollection(this.knownMappers.keySet());
    }

    public void addMappers(String packageName, Class<?> superType)
    {
//        ResolverUtil resolverUtil = new ResolverUtil();
//        resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
//        Set<Class> mapperSet = resolverUtil.getClasses();

        Set<Class> mapperSet =null;
        for (Class mapperClass : mapperSet)
            addMapper(mapperClass);
    }

    //注册某个包下面所有的Mapper接口类
    public void addMappers(String packageName)
    {
        addMappers(packageName, Object.class);
    }
}
