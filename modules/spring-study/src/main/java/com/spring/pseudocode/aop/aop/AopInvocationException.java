package com.spring.pseudocode.aop.aop;

import org.springframework.core.NestedRuntimeException;

public class AopInvocationException extends NestedRuntimeException
{
    public AopInvocationException(String msg)
    {
        super(msg);
    }

    public AopInvocationException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
