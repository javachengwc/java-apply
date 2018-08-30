package com.spring.pseudocode.beans;

import com.spring.pseudocode.core.core.MethodParameter;

import java.lang.reflect.Field;

public abstract interface TypeConverter
{
    public abstract <T> T convertIfNecessary(Object paramObject, Class<T> paramClass);

    public abstract <T> T convertIfNecessary(Object paramObject, Class<T> paramClass, MethodParameter paramMethodParameter);

    public abstract <T> T convertIfNecessary(Object paramObject, Class<T> paramClass, Field paramField);
}
