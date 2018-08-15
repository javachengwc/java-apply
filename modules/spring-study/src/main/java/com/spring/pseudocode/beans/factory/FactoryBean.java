package com.spring.pseudocode.beans.factory;

public abstract interface FactoryBean<T>
{
    public abstract T getObject()  throws Exception;

    public abstract Class<?> getObjectType();

    public abstract boolean isSingleton();
}
