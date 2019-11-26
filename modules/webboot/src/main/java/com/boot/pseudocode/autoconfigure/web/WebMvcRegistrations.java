package com.boot.pseudocode.autoconfigure.web;

import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

//springboot对webmvc的扩展点，webmvc注册器
public interface WebMvcRegistrations {

    RequestMappingHandlerMapping getRequestMappingHandlerMapping();

    RequestMappingHandlerAdapter getRequestMappingHandlerAdapter();

    ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver();
}