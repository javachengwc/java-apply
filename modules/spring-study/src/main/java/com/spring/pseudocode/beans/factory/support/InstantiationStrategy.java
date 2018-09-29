package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.BeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

//创建bean实例的策略
public abstract interface InstantiationStrategy
{
    public abstract Object instantiate(RootBeanDefinition rootBeanDefinition, String param, BeanFactory beanFactory) throws BeansException;

    public abstract Object instantiate(RootBeanDefinition rootBeanDefinition, String param, BeanFactory beanFactory,
                                       Constructor<?> constructor, Object[] arrayOfObject)  throws BeansException;

    public abstract Object instantiate(RootBeanDefinition rootBeanDefinition, String param, BeanFactory beanFactory,
                                       Object object, Method method, Object[] arrayOfObject) throws BeansException;
}
