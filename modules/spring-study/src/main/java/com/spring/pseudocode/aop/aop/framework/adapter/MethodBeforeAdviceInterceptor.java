package com.spring.pseudocode.aop.aop.framework.adapter;

import com.spring.pseudocode.aop.aop.MethodBeforeAdvice;
import com.spring.pseudocode.aop.aopalliance.intercept.MethodInterceptor;
import com.spring.pseudocode.aop.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;

public class MethodBeforeAdviceInterceptor implements MethodInterceptor, Serializable
{
    private MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice)
    {
        this.advice = advice;
    }

    public Object invoke(MethodInvocation mi) throws Throwable
    {
        this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}
