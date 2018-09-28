package com.spring.pseudocode.beans.factory;

public abstract interface HierarchicalBeanFactory extends BeanFactory
{
    public abstract BeanFactory getParentBeanFactory();

    public abstract boolean containsLocalBean(String paramString);
}
