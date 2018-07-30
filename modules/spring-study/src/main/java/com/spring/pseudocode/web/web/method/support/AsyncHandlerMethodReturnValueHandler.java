package com.spring.pseudocode.web.web.method.support;

import com.spring.pseudocode.core.core.MethodParameter;

public abstract interface AsyncHandlerMethodReturnValueHandler extends HandlerMethodReturnValueHandler
{
    public abstract boolean isAsyncReturnValue(Object paramObject, MethodParameter paramMethodParameter);
}
