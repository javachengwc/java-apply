package com.spring.pseudocode.core.cglib.proxy;

public abstract interface Factory
{
    public abstract Object newInstance(Callback paramCallback);

    public abstract Object newInstance(Callback[] paramArrayOfCallback);

    public abstract Object newInstance(Class[] paramArrayOfClass, Object[] paramArrayOfObject, Callback[] paramArrayOfCallback);

    public abstract Callback getCallback(int paramInt);

    public abstract void setCallback(int paramInt, Callback paramCallback);

    public abstract void setCallbacks(Callback[] paramArrayOfCallback);

    public abstract Callback[] getCallbacks();
}
