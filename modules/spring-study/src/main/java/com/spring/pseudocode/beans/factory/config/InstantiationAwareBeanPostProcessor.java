package com.spring.pseudocode.beans.factory.config;

import com.spring.pseudocode.beans.BeansException;
import org.springframework.beans.PropertyValues;

import java.beans.PropertyDescriptor;

public abstract interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor
{
    public abstract Object postProcessBeforeInstantiation(Class<?> clazz, String param) throws BeansException;

    public abstract boolean postProcessAfterInstantiation(Object object, String param) throws BeansException;

    public abstract PropertyValues postProcessPropertyValues(PropertyValues propertyValues, PropertyDescriptor[] arrayOfPropertyDescriptor,
                                                             Object object, String param) throws BeansException;
}
