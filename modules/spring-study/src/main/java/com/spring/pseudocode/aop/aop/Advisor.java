package com.spring.pseudocode.aop.aop;

import com.spring.pseudocode.aop.aopalliance.aop.Advice;

public abstract interface Advisor
{
    public abstract Advice getAdvice();

    public abstract boolean isPerInstance();
}
