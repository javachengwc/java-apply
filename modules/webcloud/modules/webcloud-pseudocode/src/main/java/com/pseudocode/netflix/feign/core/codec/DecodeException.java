package com.pseudocode.netflix.feign.core.codec;


import com.pseudocode.netflix.feign.core.FeignException;
import com.pseudocode.netflix.feign.core.Util;

public class DecodeException extends FeignException
{
    private static final long serialVersionUID = 1L;

    public DecodeException(String message)
    {
        super((String)Util.checkNotNull(message, "message", new Object[0]));
    }

    public DecodeException(String message, Throwable cause)
    {
        super(message, (Throwable)Util.checkNotNull(cause, "cause", new Object[0]));
    }
}
