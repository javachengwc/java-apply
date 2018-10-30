package com.spring.pseudocode.beans.factory;

public abstract interface BeanNameAware extends Aware
{
    public abstract void setBeanName(String beanName);
}
