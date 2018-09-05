package com.spring.pseudocode.aop.aop.framework.adapter;

import com.spring.pseudocode.aop.aop.Advisor;
import com.spring.pseudocode.aop.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.adapter.UnknownAdviceTypeException;

public abstract interface AdvisorAdapterRegistry
{
    public abstract Advisor wrap(Object paramObject) throws UnknownAdviceTypeException;

    public abstract MethodInterceptor[] getInterceptors(Advisor paramAdvisor) throws UnknownAdviceTypeException;

    public abstract void registerAdvisorAdapter(AdvisorAdapter paramAdvisorAdapter);
}
