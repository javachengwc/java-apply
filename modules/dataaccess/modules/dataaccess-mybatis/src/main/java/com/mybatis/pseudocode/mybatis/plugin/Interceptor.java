package com.mybatis.pseudocode.mybatis.plugin;

import java.util.Properties;

//Mybatis拦截器只能拦截四种类型的接口：
//Executor、StatementHandler、ParameterHandler和ResultSetHandler
public abstract interface Interceptor
{
    //拦截处理方法
    public abstract Object intercept(Invocation invocation) throws Throwable;

    //plugin方法中可以决定是否要进行拦截进而决定要返回一个什么样的目标对象
    //可以返回目标对象本身，也可以返回一个它的代理
    //对于plugin方,Mybatis已提供一个实现。Plugin类有一个静态方法wrap(Object target,Interceptor interceptor)，
    //通过该方法可以决定要返回的对象是目标对象还是对应的代理
    public abstract Object plugin(Object object);

    public abstract void setProperties(Properties props);
}
