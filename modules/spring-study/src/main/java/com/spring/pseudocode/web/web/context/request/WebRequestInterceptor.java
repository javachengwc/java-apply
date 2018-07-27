package com.spring.pseudocode.web.web.context.request;

import com.spring.pseudocode.context.ui.ModelMap;

public abstract interface WebRequestInterceptor
{
    public abstract void preHandle(WebRequest request) throws Exception;

    public abstract void postHandle(WebRequest request, ModelMap modelMap) throws Exception;

    public abstract void afterCompletion(WebRequest request, Exception e) throws Exception;
}
