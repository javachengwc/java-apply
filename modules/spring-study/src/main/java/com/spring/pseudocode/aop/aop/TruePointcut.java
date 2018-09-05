package com.spring.pseudocode.aop.aop;

import java.io.Serializable;

public class TruePointcut implements Pointcut, Serializable
{
    public static final TruePointcut INSTANCE = new TruePointcut();

    public ClassFilter getClassFilter()
    {
        return ClassFilter.TRUE;
    }

    public MethodMatcher getMethodMatcher()
    {
        return MethodMatcher.TRUE;
    }

    private Object readResolve()
    {
        return INSTANCE;
    }

    public String toString()
    {
        return "Pointcut.TRUE";
    }
}