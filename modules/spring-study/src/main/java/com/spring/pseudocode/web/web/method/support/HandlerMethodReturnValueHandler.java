package com.spring.pseudocode.web.web.method.support;

import com.spring.pseudocode.core.core.MethodParameter;
import com.spring.pseudocode.web.web.context.request.NativeWebRequest;

public abstract interface HandlerMethodReturnValueHandler
{
    public abstract boolean supportsReturnType(MethodParameter methodParameter);

    public abstract void handleReturnValue(Object paramObject, MethodParameter methodParameter,
                                           ModelAndViewContainer modelAndViewContainer, NativeWebRequest paramNativeWebRequest) throws Exception;
}
