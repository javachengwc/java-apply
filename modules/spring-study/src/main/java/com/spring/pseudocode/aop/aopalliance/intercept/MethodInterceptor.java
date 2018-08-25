package com.spring.pseudocode.aop.aopalliance.intercept;

public abstract interface MethodInterceptor extends Interceptor
{
    public abstract Object invoke(MethodInvocation methodInvocation)  throws Throwable;
}
