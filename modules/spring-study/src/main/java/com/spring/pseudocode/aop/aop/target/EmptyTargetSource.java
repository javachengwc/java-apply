package com.spring.pseudocode.aop.aop.target;

import com.spring.pseudocode.aop.aop.TargetSource;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

public class EmptyTargetSource implements TargetSource, Serializable
{
    private static final long serialVersionUID = 3680494563553489691L;

    public static final EmptyTargetSource INSTANCE = new EmptyTargetSource(null, true);

    private final Class<?> targetClass;

    private final boolean isStatic;

    public static EmptyTargetSource forClass(Class<?> targetClass)
    {
        return forClass(targetClass, true);
    }

    public static EmptyTargetSource forClass(Class<?> targetClass, boolean isStatic)
    {
        return (targetClass == null) && (isStatic) ? INSTANCE : new EmptyTargetSource(targetClass, isStatic);
    }

    private EmptyTargetSource(Class<?> targetClass, boolean isStatic)
    {
        this.targetClass = targetClass;
        this.isStatic = isStatic;
    }

    public Class<?> getTargetClass()
    {
        return this.targetClass;
    }

    public boolean isStatic()
    {
        return this.isStatic;
    }

    public Object getTarget()
    {
        return null;
    }

    public void releaseTarget(Object target)
    {
    }

    private Object readResolve()
    {
        return (this.targetClass == null) && (this.isStatic) ? INSTANCE : this;
    }

}
