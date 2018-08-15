package com.spring.pseudocode.beans.factory;

import com.spring.pseudocode.beans.BeansException;

public class NoSuchBeanDefinitionException extends BeansException {

    private String beanName;

    public NoSuchBeanDefinitionException(String name)
    {
        //...
    }

    public NoSuchBeanDefinitionException(String name, String message)
    {
        //...
    }
}
