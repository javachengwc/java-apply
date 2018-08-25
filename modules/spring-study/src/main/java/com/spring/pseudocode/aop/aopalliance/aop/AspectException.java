package com.spring.pseudocode.aop.aopalliance.aop;

public class AspectException extends RuntimeException
{
    public AspectException(String message)
    {
        super(message);
    }

    public AspectException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
