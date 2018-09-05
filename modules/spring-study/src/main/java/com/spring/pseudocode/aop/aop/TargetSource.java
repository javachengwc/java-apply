package com.spring.pseudocode.aop.aop;

public abstract interface TargetSource extends TargetClassAware
{
    public abstract Class<?> getTargetClass();

    public abstract boolean isStatic();

    public abstract Object getTarget() throws Exception;

    public abstract void releaseTarget(Object object) throws Exception;
}
