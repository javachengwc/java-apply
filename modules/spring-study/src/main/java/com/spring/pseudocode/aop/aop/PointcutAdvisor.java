package com.spring.pseudocode.aop.aop;

public abstract interface PointcutAdvisor extends Advisor
{
    public abstract Pointcut getPointcut();
}
