package com.spring.pseudocode.webmvc.mvc;

import com.spring.pseudocode.core.core.Ordered;
import com.spring.pseudocode.web.web.method.HandlerMethod;
import com.spring.pseudocode.webmvc.HandlerAdapter;
import com.spring.pseudocode.webmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractHandlerMethodAdapter implements HandlerAdapter, Ordered {
    private int order = Integer.MAX_VALUE;

    public AbstractHandlerMethodAdapter() {

    }

    public final boolean supports(Object handler) {
        return ((handler instanceof HandlerMethod)) && (supportsInternal((HandlerMethod) handler));
    }

    protected abstract boolean supportsInternal(HandlerMethod handlerMethod);

    public final ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        return handleInternal(request, response, (HandlerMethod)handler);
    }

    protected abstract ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception;
}

