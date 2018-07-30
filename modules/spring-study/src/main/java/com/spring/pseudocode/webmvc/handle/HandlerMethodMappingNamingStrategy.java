package com.spring.pseudocode.webmvc.handle;


import com.spring.pseudocode.web.web.method.HandlerMethod;

public abstract interface HandlerMethodMappingNamingStrategy<T>
{
    public abstract String getName(HandlerMethod paramHandlerMethod, T paramT);
}
