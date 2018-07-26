package com.spring.pseudocode.webmvc.mvc;

import com.spring.pseudocode.webmvc.HandlerAdapter;
import com.spring.pseudocode.webmvc.ModelAndView;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//HttpRequestHandlerAdapter handlerAdapter的实现,目前几乎没啥用，只是了解下它的实现
public class HttpRequestHandlerAdapter implements HandlerAdapter
{
    public boolean supports(Object handler)
    {
        return handler instanceof HttpRequestHandler;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        ((HttpRequestHandler)handler).handleRequest(request, response);
        return null;
    }

    public long getLastModified(HttpServletRequest request, Object handler)
    {
//        if ((handler instanceof LastModified)) {
//            return ((LastModified)handler).getLastModified(request);
//        }
        return -1L;
    }
}
