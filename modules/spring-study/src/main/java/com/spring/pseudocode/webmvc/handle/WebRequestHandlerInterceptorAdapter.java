package com.spring.pseudocode.webmvc.handle;


import com.spring.pseudocode.web.web.context.request.AsyncWebRequestInterceptor;
import com.spring.pseudocode.web.web.context.request.WebRequestInterceptor;
import com.spring.pseudocode.webmvc.AsyncHandlerInterceptor;
import com.spring.pseudocode.webmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebRequestHandlerInterceptorAdapter implements AsyncHandlerInterceptor
{
    private final WebRequestInterceptor requestInterceptor;

    public WebRequestHandlerInterceptorAdapter(WebRequestInterceptor requestInterceptor)
    {
        this.requestInterceptor = requestInterceptor;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        this.requestInterceptor.preHandle(new DispatcherServletWebRequest(request, response));
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {
        this.requestInterceptor.postHandle(new DispatcherServletWebRequest(request, response), (modelAndView != null) &&
                (!modelAndView .wasCleared()) ?
                modelAndView.getModelMap() : null);
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {
        this.requestInterceptor.afterCompletion(new DispatcherServletWebRequest(request, response), ex);
    }

    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        if ((this.requestInterceptor instanceof AsyncWebRequestInterceptor)) {
            AsyncWebRequestInterceptor asyncInterceptor = (AsyncWebRequestInterceptor)this.requestInterceptor;
            DispatcherServletWebRequest webRequest = new DispatcherServletWebRequest(request, response);
            asyncInterceptor.afterConcurrentHandlingStarted(webRequest);
        }
    }
}
