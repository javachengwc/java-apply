package com.mybatis.pseudocode.mybatis.executor.resultset;


import java.util.HashMap;
import java.util.Map;

public class PrimitiveTypes
{
    private final Map<Class<?>, Class<?>> primitiveToWrappers;
    private final Map<Class<?>, Class<?>> wrappersToPrimitives;

    public PrimitiveTypes()
    {
        this.primitiveToWrappers = new HashMap();
        this.wrappersToPrimitives = new HashMap();

        add(Boolean.TYPE, Boolean.class);
        add(Byte.TYPE, Byte.class);
        add(Character.TYPE, Character.class);
        add(Double.TYPE, Double.class);
        add(Float.TYPE, Float.class);
        add(Integer.TYPE, Integer.class);
        add(Long.TYPE, Long.class);
        add(Short.TYPE, Short.class);
        add(Void.TYPE, Void.class);
    }

    private void add(Class<?> primitiveType, Class<?> wrapperType) {
        this.primitiveToWrappers.put(primitiveType, wrapperType);
        this.wrappersToPrimitives.put(wrapperType, primitiveType);
    }

    public Class<?> getWrapper(Class<?> primitiveType) {
        return (Class)this.primitiveToWrappers.get(primitiveType);
    }

    public Class<?> getPrimitive(Class<?> wrapperType) {
        return (Class)this.wrappersToPrimitives.get(wrapperType);
    }
}
