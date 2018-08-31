package com.spring.pseudocode.beans.factory.config;

import com.spring.pseudocode.beans.BeansException;

public abstract interface BeanPostProcessor
{
    //BeanPostProcessor前置处理
    public abstract Object postProcessBeforeInitialization(Object paramObject, String paramString)  throws BeansException;

    //BeanPostProcessor后置处理
    public abstract Object postProcessAfterInitialization(Object paramObject, String paramString) throws BeansException;
}
