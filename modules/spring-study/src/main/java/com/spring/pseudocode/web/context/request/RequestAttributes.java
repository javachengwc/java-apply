package com.spring.pseudocode.web.context.request;

public abstract interface RequestAttributes
{
    public abstract Object getAttribute(String paramString, int paramInt);

    public abstract void setAttribute(String paramString, Object paramObject, int paramInt);

    public abstract String getSessionId();

}
