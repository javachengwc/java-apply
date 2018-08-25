package com.spring.pseudocode.aop.aop;

//切入点
public abstract interface Pointcut
{
    public abstract ClassFilter getClassFilter();

    public abstract MethodMatcher getMethodMatcher();
}
