package com.spring.pseudocode.beans.factory.config;

import org.springframework.beans.BeansException;

import java.lang.reflect.Constructor;

public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {

    Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException;

    Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException;

    //getEarlyBeanReference是为了解决单例bean之间的循环依赖问题，提前将代理对象暴露出去
    Object getEarlyBeanReference(Object bean, String beanName) throws BeansException;

}

