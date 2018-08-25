package com.spring.pseudocode.aop.aop;

import com.spring.pseudocode.aop.aopalliance.aop.Advice;

//切面
public abstract interface Advisor
{
    public abstract Advice getAdvice();

    public abstract boolean isPerInstance();
}
