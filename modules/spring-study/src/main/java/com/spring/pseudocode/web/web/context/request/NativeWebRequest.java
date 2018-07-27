package com.spring.pseudocode.web.web.context.request;

public abstract interface NativeWebRequest extends WebRequest
{
    public abstract Object getNativeRequest();

    public abstract Object getNativeResponse();

    public abstract <T> T getNativeRequest(Class<T> paramClass);

    public abstract <T> T getNativeResponse(Class<T> paramClass);
}
