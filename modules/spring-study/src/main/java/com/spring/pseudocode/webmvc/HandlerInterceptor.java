package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//拦截器接口
public abstract interface HandlerInterceptor {

    //前置处理,在controller方法调用之前执行
    public abstract boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object paramObject)
            throws Exception;

    //后置处理,在controller方法调用之前执行
    public abstract void postHandle(HttpServletRequest request, HttpServletResponse response, Object paramObject, ModelAndView paramModelAndView)
            throws Exception;

    //完成后返回结果处理
    public abstract void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object paramObject, Exception paramException)
            throws Exception;
}
