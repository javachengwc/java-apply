package com.spring.pseudocode.aop.aop.framework;

//AopProxy是Spring Aop提供的代理类
public abstract interface AopProxy
{
    //获取一个代理对象
    public abstract Object getProxy();

    //根据类加载器获取代理对象
    public abstract Object getProxy(ClassLoader classLoader);
}
