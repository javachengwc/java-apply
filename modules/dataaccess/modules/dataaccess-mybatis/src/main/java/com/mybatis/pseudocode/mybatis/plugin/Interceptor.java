package com.mybatis.pseudocode.mybatis.plugin;

import java.util.Properties;

public abstract interface Interceptor
{
    public abstract Object intercept(Invocation invocation) throws Throwable;

    public abstract Object plugin(Object object);

    public abstract void setProperties(Properties props);
}
