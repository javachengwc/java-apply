package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface HandlerAdapter
{
    public abstract boolean supports(Object paramObject);

    public abstract ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object paramObject) throws Exception;

    public abstract long getLastModified(HttpServletRequest request, Object paramObject);
}
