package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//异步处理拦截器
public abstract interface AsyncHandlerInterceptor extends HandlerInterceptor
{
    public abstract void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object paramObject) throws Exception;
}