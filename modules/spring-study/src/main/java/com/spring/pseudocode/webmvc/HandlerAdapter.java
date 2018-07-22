package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface HandlerAdapter
{
    // 当前 HandlerAdapter 是否支持Handler
    public abstract boolean supports(Object paramObject);

    // 利用 Handler 处理请求
    public abstract ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object paramObject) throws Exception;

    public abstract long getLastModified(HttpServletRequest request, Object paramObject);
}
