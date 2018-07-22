package com.spring.pseudocode.webmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//springmvc中HandlerExecutionChain的伪代码
public class HandlerExecutionChain {

    private static Logger logger= LoggerFactory.getLogger(HandlerExecutionChain.class);

    private final Object handler=null;

    private HandlerInterceptor[] interceptors;

    private List<HandlerInterceptor> interceptorList;

    private int interceptorIndex = -1;

    public HandlerExecutionChain(Object handler)
    {
        this(handler, (HandlerInterceptor[])null);
    }

    public HandlerExecutionChain(Object handler, HandlerInterceptor[] interceptors)
    {

        //this.handler = handler;
        this.interceptors = interceptors;
    }

    public Object getHandler()
    {
        return this.handler;
    }

    public HandlerInterceptor[] getInterceptors()
    {
        if ((this.interceptors == null) && (this.interceptorList != null)) {
            this.interceptors = ((HandlerInterceptor[])this.interceptorList.toArray(new HandlerInterceptor[this.interceptorList.size()]));
        }
        return this.interceptors;
    }

    //在controller方法调用之前执行拦截器的preHandle
    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response)  throws Exception
    {
        HandlerInterceptor[] interceptors = getInterceptors();
        for (int i = 0; i < interceptors.length; i++) {
            HandlerInterceptor interceptor = interceptors[i];
            if (!interceptor.preHandle(request, response, this.handler)) {
                triggerAfterCompletion(request, response, null);
                return false;
            }
            this.interceptorIndex = i;
        }
        return true;
    }

    //在controller方法调用之后执行拦截器的postHandle,拦截器在这里有机会修改ModelAndView。
    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception
    {
        HandlerInterceptor[] interceptors = getInterceptors();
        for (int i = interceptors.length - 1; i >= 0; i--) {
            HandlerInterceptor interceptor = interceptors[i];
            interceptor.postHandle(request, response, this.handler, mv);
        }
    }

    //结果返回处理
    public void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception
    {
        HandlerInterceptor[] interceptors = getInterceptors();
        for (int i = this.interceptorIndex; i >= 0; i--) {
            HandlerInterceptor interceptor = interceptors[i];
            try {
                interceptor.afterCompletion(request, response, this.handler, ex);
            }
            catch (Throwable ex2) {
                logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
            }
        }
    }

}
