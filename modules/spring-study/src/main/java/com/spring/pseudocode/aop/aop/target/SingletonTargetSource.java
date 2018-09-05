package com.spring.pseudocode.aop.aop.target;

import com.spring.pseudocode.aop.aop.TargetSource;
import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;

public class SingletonTargetSource implements TargetSource, Serializable
{
    private static final long serialVersionUID = 9031246629662423738L;
    private final Object target;

    public SingletonTargetSource(Object target)
    {
        this.target = target;
    }

    public Class<?> getTargetClass()
    {
        return this.target.getClass();
    }

    public Object getTarget()
    {
        return this.target;
    }

    public void releaseTarget(Object target)
    {
    }

    public boolean isStatic()
    {
        return true;
    }

}
