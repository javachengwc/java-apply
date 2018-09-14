package com.spring.pseudocode.context.context.annotation;

import com.spring.pseudocode.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;

public abstract interface ImportBeanDefinitionRegistrar
{
    public abstract void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry);
}
