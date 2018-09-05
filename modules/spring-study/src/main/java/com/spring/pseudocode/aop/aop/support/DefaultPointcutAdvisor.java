package com.spring.pseudocode.aop.aop.support;

import com.spring.pseudocode.aop.aop.Pointcut;
import com.spring.pseudocode.aop.aopalliance.aop.Advice;

import java.io.Serializable;

public class DefaultPointcutAdvisor extends AbstractGenericPointcutAdvisor implements Serializable
{
    private Pointcut pointcut = Pointcut.TRUE;

    public DefaultPointcutAdvisor()
    {
    }

    public DefaultPointcutAdvisor(Advice advice)
    {
        this(Pointcut.TRUE, advice);
    }

    public DefaultPointcutAdvisor(Pointcut pointcut, Advice advice)
    {
        this.pointcut = pointcut;
        setAdvice(advice);
    }

    public void setPointcut(Pointcut pointcut)
    {
        this.pointcut = (pointcut != null ? pointcut : Pointcut.TRUE);
    }

    public Pointcut getPointcut()
    {
        return this.pointcut;
    }

    public String toString()
    {
        return getClass().getName() + ": pointcut [" + getPointcut() + "]; advice [" + getAdvice() + "]";
    }
}
