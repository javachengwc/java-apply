package com.spring.pseudocode.aop.aop;

import java.lang.reflect.Method;

public abstract interface MethodBeforeAdvice extends BeforeAdvice
{
    public abstract void before(Method paramMethod, Object[] paramArrayOfObject, Object paramObject) throws Throwable;
}
