package com.spring.pseudocode.web.web.method.support;

import com.spring.pseudocode.core.core.MethodParameter;
import com.spring.pseudocode.web.web.bind.support.WebDataBinderFactory;
import com.spring.pseudocode.web.web.context.request.NativeWebRequest;

public abstract interface HandlerMethodArgumentResolver
{
    public abstract boolean supportsParameter(MethodParameter methodParameter);

    public abstract Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                           NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception;
}
