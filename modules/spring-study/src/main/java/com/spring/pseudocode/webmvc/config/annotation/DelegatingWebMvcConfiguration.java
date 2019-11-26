package com.spring.pseudocode.webmvc.config.annotation;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//检测Spring容器中有多少WebMvcConfigurer，然后把它加入进来
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {

    private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();

    //收集BeanFactory中所有WebMvcConfigurer的实现，汇集到WebMvcConfigurerComposite中
    @Autowired(required = false)
    public void setConfigurers(List<WebMvcConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addWebMvcConfigurers(configurers);
        }
    }


    @Override
    protected void configurePathMatch(PathMatchConfigurer configurer) {
        this.configurers.configurePathMatch(configurer);
    }

    @Override
    protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        this.configurers.configureContentNegotiation(configurer);
    }

    @Override
    protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        this.configurers.configureAsyncSupport(configurer);
    }

    @Override
    protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        this.configurers.configureDefaultServletHandling(configurer);
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        this.configurers.addFormatters(registry);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        this.configurers.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        this.configurers.addResourceHandlers(registry);
    }

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        this.configurers.addCorsMappings(registry);
    }

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        this.configurers.addViewControllers(registry);
    }

    @Override
    protected void configureViewResolvers(ViewResolverRegistry registry) {
        this.configurers.configureViewResolvers(registry);
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        this.configurers.addArgumentResolvers(argumentResolvers);
    }

    @Override
    protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        this.configurers.addReturnValueHandlers(returnValueHandlers);
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        this.configurers.configureMessageConverters(converters);
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        this.configurers.extendMessageConverters(converters);
    }

    @Override
    protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        this.configurers.configureHandlerExceptionResolvers(exceptionResolvers);
    }

    @Override
    protected void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        this.configurers.extendHandlerExceptionResolvers(exceptionResolvers);
    }

    @Override
    protected Validator getValidator() {
        return this.configurers.getValidator();
    }

    @Override
    protected MessageCodesResolver getMessageCodesResolver() {
        return this.configurers.getMessageCodesResolver();
    }

}
