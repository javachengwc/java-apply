package com.spring.pseudocode.aop.aop;

public abstract interface PointcutAdvisor extends Advisor
{
    //获取切入点
    public abstract Pointcut getPointcut();
}
