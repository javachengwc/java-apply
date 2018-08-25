package com.spring.pseudocode.aop.aopalliance.intercept;

import java.lang.reflect.AccessibleObject;

public abstract interface Joinpoint
{
    public abstract Object proceed()  throws Throwable;

    public abstract Object getThis();

    public abstract AccessibleObject getStaticPart();
}
