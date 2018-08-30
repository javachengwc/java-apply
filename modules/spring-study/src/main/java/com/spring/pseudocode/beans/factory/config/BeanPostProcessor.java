package com.spring.pseudocode.beans.factory.config;

import com.spring.pseudocode.beans.BeansException;

public abstract interface BeanPostProcessor
{
    public abstract Object postProcessBeforeInitialization(Object paramObject, String paramString)  throws BeansException;

    public abstract Object postProcessAfterInitialization(Object paramObject, String paramString) throws BeansException;
}
