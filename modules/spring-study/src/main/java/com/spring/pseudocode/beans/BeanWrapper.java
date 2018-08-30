package com.spring.pseudocode.beans;

import org.springframework.beans.InvalidPropertyException;

import java.beans.PropertyDescriptor;

public abstract interface BeanWrapper
{
    public abstract void setAutoGrowCollectionLimit(int paramInt);

    public abstract int getAutoGrowCollectionLimit();

    public abstract Object getWrappedInstance();

    public abstract Class<?> getWrappedClass();

    public abstract PropertyDescriptor[] getPropertyDescriptors();

    public abstract PropertyDescriptor getPropertyDescriptor(String paramString) throws InvalidPropertyException;
}
