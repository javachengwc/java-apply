package com.spring.pseudocode.core.objenesis;

import com.spring.pseudocode.core.objenesis.instantiator.ObjectInstantiator;
import org.springframework.objenesis.strategy.InstantiatorStrategy;

public class SpringObjenesis  implements Objenesis
{
    public static final String IGNORE_OBJENESIS_PROPERTY_NAME = "spring.objenesis.ignore";
    private volatile Boolean worthTrying;

    public SpringObjenesis()
    {
        this(null);
    }

    public SpringObjenesis(InstantiatorStrategy strategy)
    {
        //...
    }

    public boolean isWorthTrying()
    {
        return this.worthTrying != Boolean.FALSE;
    }

    public <T> T newInstance(Class<T> clazz, boolean useCache)
    {
        //...
        return null;
    }

    public <T> T newInstance(Class<T> clazz) {
        //...
        return null;
    }

    public <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> clazz)
    {
        //...
        return null;
    }

    protected <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> clazz) {
        //...
        return null;
    }
}