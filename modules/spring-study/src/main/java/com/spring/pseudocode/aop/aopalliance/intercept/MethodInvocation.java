package com.spring.pseudocode.aop.aopalliance.intercept;

import java.lang.reflect.Method;

public abstract interface MethodInvocation extends Invocation
{
    public abstract Method getMethod();
}
