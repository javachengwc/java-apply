package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.factory.config.BeanDefinition;

public abstract interface BeanNameGenerator
{
    public abstract String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry);
}
