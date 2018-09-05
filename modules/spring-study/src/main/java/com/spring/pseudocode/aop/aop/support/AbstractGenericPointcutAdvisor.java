package com.spring.pseudocode.aop.aop.support;

import com.spring.pseudocode.aop.aopalliance.aop.Advice;

public abstract class AbstractGenericPointcutAdvisor extends AbstractPointcutAdvisor
{
    private Advice advice;

    public void setAdvice(Advice advice)
    {
        this.advice = advice;
    }

    public Advice getAdvice()
    {
        return this.advice;
    }

    public String toString()
    {
        return getClass().getName() + ": advice [" + getAdvice() + "]";
    }
}