package com.spring.pseudocode.core.objenesis;

import com.spring.pseudocode.core.objenesis.instantiator.ObjectInstantiator;

public abstract interface Objenesis
{
    public abstract <T> T newInstance(Class<T> paramClass);

    public abstract <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> paramClass);
}
