package com.spring.pseudocode.beans.factory;


import com.spring.pseudocode.beans.BeansException;
import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;
import java.util.Map;

public abstract interface ListableBeanFactory extends BeanFactory
{
    public abstract boolean containsBeanDefinition(String paramString);

    public abstract int getBeanDefinitionCount();

    public abstract String[] getBeanDefinitionNames();

    public abstract String[] getBeanNamesForType(ResolvableType paramResolvableType);

    public abstract String[] getBeanNamesForType(Class<?> paramClass);

    public abstract String[] getBeanNamesForType(Class<?> paramClass, boolean paramBoolean1, boolean paramBoolean2);

    public abstract <T> Map<String, T> getBeansOfType(Class<T> paramClass) throws BeansException;

    public abstract <T> Map<String, T> getBeansOfType(Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2) throws BeansException;

    public abstract String[] getBeanNamesForAnnotation(Class<? extends Annotation> paramClass);

    public abstract Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> paramClass) throws BeansException;

    public abstract <A extends Annotation> A findAnnotationOnBean(String paramString, Class<A> paramClass) throws NoSuchBeanDefinitionException;
}
