package com.spring.pseudocode.web.web.method.support;

import com.spring.pseudocode.core.core.MethodParameter;
import com.spring.pseudocode.web.web.context.request.NativeWebRequest;

public abstract interface HandlerMethodReturnValueHandler
{
    public abstract boolean supportsReturnType(MethodParameter methodParameter);

    //处理返回结果
    public abstract void handleReturnValue(Object paramObject, MethodParameter methodParameter,
                                           ModelAndViewContainer modelAndViewContainer, NativeWebRequest paramNativeWebRequest) throws Exception;
}
