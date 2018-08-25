package com.spring.pseudocode.aop.aopalliance.intercept;

public abstract interface Invocation extends Joinpoint
{
    public abstract Object[] getArguments();
}
