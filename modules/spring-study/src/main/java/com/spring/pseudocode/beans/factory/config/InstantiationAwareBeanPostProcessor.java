package com.spring.pseudocode.beans.factory.config;

import com.spring.pseudocode.beans.BeansException;
import org.springframework.beans.PropertyValues;

import java.beans.PropertyDescriptor;

/**
 *InstantiantionAwareBeanPostProcessor是在bean对象被创建的之前调用postProcessBeforeInstantiation()方法，若此方法返回的结果不为null，则会中断后面bean对象的创建过程。
 *InstantiantionAwareBeanPostProcessor.postProcessAfterInstantiation()是在对象被创建之后，属性注入之前调用，
 *若该方法返回false，会中断属性的注入，并且也会中断InstantiantionAwareBeanPostProcessor链在当前bean上的调用。
**/
public abstract interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor
{
    public abstract Object postProcessBeforeInstantiation(Class<?> clazz, String param) throws BeansException;

    public abstract boolean postProcessAfterInstantiation(Object object, String param) throws BeansException;

    public abstract PropertyValues postProcessPropertyValues(PropertyValues propertyValues, PropertyDescriptor[] arrayOfPropertyDescriptor,
                                                             Object object, String param) throws BeansException;
}
