package com.mybatis.pseudocode.mybatis.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invocation
{
    private final Object target;
    private final Method method;
    private final Object[] args;

    public Invocation(Object target, Method method, Object[] args)
    {
        this.target = target;
        this.method = method;
        this.args = args;
    }

    public Object getTarget() {
        return this.target;
    }

    public Method getMethod() {
        return this.method;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public Object proceed() throws InvocationTargetException, IllegalAccessException {
        return this.method.invoke(this.target, this.args);
    }
}
