package com.spring.pseudocode.aop.aop;

public abstract interface Pointcut
{
    public abstract ClassFilter getClassFilter();

    public abstract MethodMatcher getMethodMatcher();
}
