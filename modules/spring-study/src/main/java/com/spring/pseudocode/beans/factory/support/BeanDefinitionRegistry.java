package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.factory.NoSuchBeanDefinitionException;
import com.spring.pseudocode.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.BeanDefinitionStoreException;

public abstract interface BeanDefinitionRegistry
{
    public abstract void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException;

    public abstract void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    public abstract BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    public abstract boolean containsBeanDefinition(String beanName);

    public abstract String[] getBeanDefinitionNames();

    public abstract int getBeanDefinitionCount();

    public abstract boolean isBeanNameInUse(String beanName);
}
