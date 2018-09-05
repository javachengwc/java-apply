package com.spring.pseudocode.aop.aop.framework.adapter;

import com.spring.pseudocode.aop.aop.Advisor;
import com.spring.pseudocode.aop.aop.support.DefaultPointcutAdvisor;
import com.spring.pseudocode.aop.aopalliance.aop.Advice;
import com.spring.pseudocode.aop.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.adapter.UnknownAdviceTypeException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultAdvisorAdapterRegistry implements AdvisorAdapterRegistry, Serializable
{
    private final List<AdvisorAdapter> adapters = new ArrayList(3);

    public DefaultAdvisorAdapterRegistry()
    {
//        registerAdvisorAdapter(new MethodBeforeAdviceAdapter());
//        registerAdvisorAdapter(new AfterReturningAdviceAdapter());
//        registerAdvisorAdapter(new ThrowsAdviceAdapter());
    }

    public Advisor wrap(Object adviceObject) throws UnknownAdviceTypeException
    {
        if ((adviceObject instanceof Advisor)) {
            return (Advisor)adviceObject;
        }
        if (!(adviceObject instanceof Advice)) {
            throw new UnknownAdviceTypeException(adviceObject);
        }
        Advice advice = (Advice)adviceObject;
        if ((advice instanceof MethodInterceptor))
        {
            return new DefaultPointcutAdvisor(advice);
        }
        for (AdvisorAdapter adapter : this.adapters)
        {
            if (adapter.supportsAdvice(advice)) {
                return new DefaultPointcutAdvisor(advice);
            }
        }
        throw new UnknownAdviceTypeException(advice);
    }

    public MethodInterceptor[] getInterceptors(Advisor advisor) throws UnknownAdviceTypeException
    {
        List interceptors = new ArrayList(3);
        Advice advice = advisor.getAdvice();
        if ((advice instanceof MethodInterceptor)) {
            interceptors.add((MethodInterceptor)advice);
        }
        for (AdvisorAdapter adapter : this.adapters) {
            if (adapter.supportsAdvice(advice)) {
                interceptors.add(adapter.getInterceptor(advisor));
            }
        }
        if (interceptors.isEmpty()) {
            throw new UnknownAdviceTypeException(advisor.getAdvice());
        }
        return (MethodInterceptor[])interceptors.toArray(new MethodInterceptor[interceptors.size()]);
    }

    public void registerAdvisorAdapter(AdvisorAdapter adapter)
    {
        this.adapters.add(adapter);
    }
}
