package com.spring.pseudocode.core.objenesis.instantiator;

public abstract interface ObjectInstantiator<T>
{
    public abstract T newInstance();
}
