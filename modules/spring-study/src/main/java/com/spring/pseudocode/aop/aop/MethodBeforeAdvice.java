package com.spring.pseudocode.aop.aop;

import java.lang.reflect.Method;

//方法前置增强
public abstract interface MethodBeforeAdvice extends BeforeAdvice
{
    public abstract void before(Method method, Object[] arrays, Object object) throws Throwable;
}
