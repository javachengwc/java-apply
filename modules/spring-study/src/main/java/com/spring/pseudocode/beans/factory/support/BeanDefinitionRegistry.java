package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.factory.NoSuchBeanDefinitionException;
import com.spring.pseudocode.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.BeanDefinitionStoreException;

public abstract interface BeanDefinitionRegistry
{
    public abstract void registerBeanDefinition(String param, BeanDefinition beanDefinition) throws BeanDefinitionStoreException;

    public abstract void removeBeanDefinition(String param) throws NoSuchBeanDefinitionException;

    public abstract BeanDefinition getBeanDefinition(String param) throws NoSuchBeanDefinitionException;

    public abstract boolean containsBeanDefinition(String param);

    public abstract String[] getBeanDefinitionNames();

    public abstract int getBeanDefinitionCount();

    public abstract boolean isBeanNameInUse(String param);
}
