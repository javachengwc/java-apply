package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.config.BeanFactoryPostProcessor;

public abstract interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor
{
    public abstract void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException;
}
