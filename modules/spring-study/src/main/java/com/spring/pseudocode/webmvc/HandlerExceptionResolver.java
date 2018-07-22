package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface HandlerExceptionResolver
{
    public abstract ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object paramObject, Exception paramException);
}
