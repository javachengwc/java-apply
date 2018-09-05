package com.spring.pseudocode.aop.aop;

public abstract interface IntroductionAdvisor extends Advisor, IntroductionInfo
{
    public abstract ClassFilter getClassFilter();

    public abstract void validateInterfaces()  throws IllegalArgumentException;
}
