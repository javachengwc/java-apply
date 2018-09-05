package com.spring.pseudocode.aop.aop.framework.adapter;

import com.spring.pseudocode.aop.aop.Advisor;
import com.spring.pseudocode.aop.aopalliance.aop.Advice;
import com.spring.pseudocode.aop.aopalliance.intercept.MethodInterceptor;

public abstract interface AdvisorAdapter
{
    public abstract boolean supportsAdvice(Advice advice);

    public abstract MethodInterceptor getInterceptor(Advisor advisor);
}
