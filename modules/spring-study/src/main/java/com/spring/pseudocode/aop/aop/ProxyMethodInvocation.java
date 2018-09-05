package com.spring.pseudocode.aop.aop;

import com.spring.pseudocode.aop.aopalliance.intercept.MethodInvocation;

public abstract interface ProxyMethodInvocation extends MethodInvocation
{
    public abstract Object getProxy();

    public abstract MethodInvocation invocableClone();

    public abstract MethodInvocation invocableClone(Object[] paramArrayOfObject);

    public abstract void setArguments(Object[] paramArrayOfObject);

    public abstract void setUserAttribute(String paramString, Object paramObject);

    public abstract Object getUserAttribute(String paramString);
}
