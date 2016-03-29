package com.ocean.util;

import java.lang.reflect.Method;

/**
 * 方法调用
 */
public class MethodInvocation {

    private Method method;

    private Object[] arguments;

    public MethodInvocation()
    {

    }

    public  MethodInvocation(Method method,Object[] arguments)
    {
        this.method=method;
        this.arguments=arguments;
    }

    //调用方法
    public void invoke(final Object target) {
        try {

            method.invoke(target, arguments);
        } catch (Exception e) {

            handleException(e);
        }
    }

    public void handleException(Exception e)
    {

    }
}
