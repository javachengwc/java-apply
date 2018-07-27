package com.spring.pseudocode.web.web.context.request;

public abstract interface AsyncWebRequestInterceptor extends WebRequestInterceptor
{
    public abstract void afterConcurrentHandlingStarted(WebRequest paramWebRequest);
}
