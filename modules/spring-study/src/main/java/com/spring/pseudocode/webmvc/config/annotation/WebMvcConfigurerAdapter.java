package com.spring.pseudocode.webmvc.config.annotation;


import com.spring.pseudocode.web.web.method.support.HandlerMethodArgumentResolver;
import com.spring.pseudocode.web.web.method.support.HandlerMethodReturnValueHandler;
import com.spring.pseudocode.webmvc.HandlerExceptionResolver;
import org.springframework.validation.MessageCodesResolver;

import java.util.List;

public abstract class WebMvcConfigurerAdapter implements WebMvcConfigurer
{

    //增加参数解析器
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
    {
    }

    //增加结果处理器
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
    {

    }

    //配置异常解析器
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
    {
    }

    public MessageCodesResolver getMessageCodesResolver()
    {
        return null;
    }

    //增加拦截器
    public void addInterceptors(InterceptorRegistry registry)
    {
    }

    public void addViewControllers(ViewControllerRegistry registry)
    {
    }

    public void configureViewResolvers(ViewResolverRegistry registry)
    {
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
    }
    public void addCorsMappings(CorsRegistry registry)
    {
    }
}
