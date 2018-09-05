package com.spring.pseudocode.aop.aop;

public abstract interface ClassFilter
{
    public static final ClassFilter TRUE = TrueClassFilter.INSTANCE;

    public abstract boolean matches(Class<?> paramClass);
}
