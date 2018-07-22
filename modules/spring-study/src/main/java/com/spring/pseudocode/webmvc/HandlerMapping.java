package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;

public abstract interface HandlerMapping {

    //HandlerMapping只有一个接口方法getHandler
    //此方法根据当前请求找到对应的 Handler，并将 Handler与一堆 HandlerInterceptor（拦截器）封装到 HandlerExecutionChain 对象中返回结果
    public abstract HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
