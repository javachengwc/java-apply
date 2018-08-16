package com.spring.pseudocode.beans.factory.config;

public abstract interface SingletonBeanRegistry
{
    public abstract void registerSingleton(String paramString, Object paramObject);

    public abstract Object getSingleton(String paramString);

    public abstract boolean containsSingleton(String paramString);

    public abstract String[] getSingletonNames();

    public abstract int getSingletonCount();

    public abstract Object getSingletonMutex();
}
