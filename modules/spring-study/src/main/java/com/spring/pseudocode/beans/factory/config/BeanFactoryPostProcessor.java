package com.spring.pseudocode.beans.factory.config;

import com.spring.pseudocode.beans.BeansException;

public abstract interface BeanFactoryPostProcessor
{
    public abstract void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException;
}
