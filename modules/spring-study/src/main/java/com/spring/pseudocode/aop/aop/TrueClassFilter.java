package com.spring.pseudocode.aop.aop;

import java.io.Serializable;

public  class  TrueClassFilter  implements ClassFilter, Serializable
{
    public static final TrueClassFilter INSTANCE = new TrueClassFilter();

    public boolean matches(Class<?> clazz)
    {
        return true;
    }

    private Object readResolve()
    {
        return INSTANCE;
    }

    public String toString()
    {
        return "ClassFilter.TRUE";
    }
}