package com.spring.pseudocode.web.context.request;

public abstract interface AsyncWebRequestInterceptor extends WebRequestInterceptor
{
    public abstract void afterConcurrentHandlingStarted(WebRequest paramWebRequest);
}
