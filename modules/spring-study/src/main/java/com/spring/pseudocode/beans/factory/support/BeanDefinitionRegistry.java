package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.factory.NoSuchBeanDefinitionException;
import com.spring.pseudocode.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.BeanDefinitionStoreException;

//BeanDefinition Registry是bean配置信息容器，提供了对beanDefinition的管理
//Spring配置文件中每一个<bean>节点元素在Spring容器里都通过一个BeanDefinition对象表示，它描述了Bean的配置信息
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
