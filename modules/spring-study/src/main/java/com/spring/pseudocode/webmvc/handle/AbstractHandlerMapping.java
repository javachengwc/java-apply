package com.spring.pseudocode.webmvc.handle;


import com.spring.pseudocode.context.context.ApplicationContext;
import com.spring.pseudocode.context.context.ApplicationContextAware;
import com.spring.pseudocode.core.core.Ordered;
import com.spring.pseudocode.webmvc.HandlerExecutionChain;
import com.spring.pseudocode.webmvc.HandlerInterceptor;
import com.spring.pseudocode.webmvc.HandlerMapping;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHandlerMapping  implements HandlerMapping, Ordered,ApplicationContextAware
{
    private int order = Integer.MAX_VALUE;

    //处理请求的相对路径为/*的Handler
    private Object defaultHandler;

    private ApplicationContext applicationContext;

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    private PathMatcher pathMatcher = new AntPathMatcher();

    private final List<Object> interceptors = new ArrayList<Object>();

    private final List<HandlerInterceptor> adaptedInterceptors = new ArrayList<HandlerInterceptor>();

    public Object getDefaultHandler()
    {
        return this.defaultHandler;
    }

    public void setDefaultHandler(Object defaultHandler) {
        this.defaultHandler=defaultHandler;
    }

    public PathMatcher getPathMatcher()
    {
        return this.pathMatcher;
    }


    public final ApplicationContext getApplicationContext()  throws IllegalStateException
    {
        return this.applicationContext;
    }

    public UrlPathHelper getUrlPathHelper()
    {
        return this.urlPathHelper;
    }

    public  void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    //根据请求找到对应的 Handler，并将 Handler（执行程序）与一堆 HandlerInterceptor（拦截器）封装到 HandlerExecutionChain 对象中返回。
    public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception
    {
        //让具体的实现类去查找对应的Handler
        Object handler = getHandlerInternal(request);
        if (handler == null) {
            handler = getDefaultHandler();
        }
        if (handler == null) {
            return null;
        }

        if ((handler instanceof String)) {
            String handlerName = (String)handler;
            handler = getApplicationContext().getBean(handlerName);
        }

        HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);
        return executionChain;
    }

    //找到对应的Handler（执行程序）
    protected abstract Object getHandlerInternal(HttpServletRequest request) throws Exception;

    //将 Handler（执行程序）与一堆 HandlerInterceptor（拦截器）封装到 HandlerExecutionChain 对象中返回
    protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request)
    {
        HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain) ? (HandlerExecutionChain)handler : new HandlerExecutionChain(handler);
        //.............
        return chain;
    }

}
