package com.spring.pseudocode.beans.factory;

import com.spring.pseudocode.beans.BeansException;

public class BeanDefinitionStoreException  extends BeansException {

    private String resourceDescription;

    private String beanName;

    public BeanDefinitionStoreException(String msg)
    {
        //...
    }

    public BeanDefinitionStoreException(String msg, Throwable cause)
    {
        //...
    }
}
