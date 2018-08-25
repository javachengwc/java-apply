package com.spring.pseudocode.aop.aop;

import java.lang.reflect.Method;

public abstract interface MethodMatcher
{
    public abstract boolean matches(Method method, Class<?> clazz);

    public abstract boolean isRuntime();

    public abstract boolean matches(Method method, Class<?> clazz, Object[] arrayOfObject);
}
