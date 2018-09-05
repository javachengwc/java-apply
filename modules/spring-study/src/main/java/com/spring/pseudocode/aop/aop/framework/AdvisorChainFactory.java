package com.spring.pseudocode.aop.aop.framework;

import java.lang.reflect.Method;
import java.util.List;

public abstract interface AdvisorChainFactory
{
    public abstract List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised advised, Method method, Class<?> clazz);
}
