package com.spring.pseudocode.aop.aop.framework.adapter;

import com.spring.pseudocode.aop.aop.Advisor;
import com.spring.pseudocode.aop.aop.MethodBeforeAdvice;
import com.spring.pseudocode.aop.aopalliance.aop.Advice;
import com.spring.pseudocode.aop.aopalliance.intercept.MethodInterceptor;

import java.io.Serializable;

public class MethodBeforeAdviceAdapter  implements AdvisorAdapter, Serializable
{
    public boolean supportsAdvice(Advice advice)
    {
        return advice instanceof MethodBeforeAdvice;
    }

    public MethodInterceptor getInterceptor(Advisor advisor)
    {
        MethodBeforeAdvice advice = (MethodBeforeAdvice)advisor.getAdvice();
        return new MethodBeforeAdviceInterceptor(advice);
    }
}