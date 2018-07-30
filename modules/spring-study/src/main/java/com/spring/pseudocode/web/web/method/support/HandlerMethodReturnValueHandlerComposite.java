package com.spring.pseudocode.web.web.method.support;

import com.spring.pseudocode.core.core.MethodParameter;
import com.spring.pseudocode.web.web.context.request.NativeWebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerMethodReturnValueHandlerComposite implements AsyncHandlerMethodReturnValueHandler
{
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();

    public List<HandlerMethodReturnValueHandler> getHandlers()
    {
        return Collections.unmodifiableList(this.returnValueHandlers);
    }

    public boolean supportsReturnType(MethodParameter returnType)
    {
        return getReturnValueHandler(returnType) != null;
    }

    private HandlerMethodReturnValueHandler getReturnValueHandler(MethodParameter returnType) {
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            if (handler.supportsReturnType(returnType)) {
                return handler;
            }
        }
        return null;
    }

    //对结果进行处理
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception
    {
        HandlerMethodReturnValueHandler handler = selectHandler(returnValue, returnType);
        if (handler == null) {
            throw new IllegalArgumentException("Unknown return value type: " + returnType.getParameterType().getName());
        }
        handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }

    private HandlerMethodReturnValueHandler selectHandler(Object value, MethodParameter returnType) {
        boolean isAsyncValue = isAsyncReturnValue(value, returnType);
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            if ((isAsyncValue) && (!(handler instanceof AsyncHandlerMethodReturnValueHandler))) {
                continue;
            }
            if (handler.supportsReturnType(returnType)) {
                return handler;
            }
        }
        return null;
    }

    public boolean isAsyncReturnValue(Object value, MethodParameter returnType)
    {
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            if (((handler instanceof AsyncHandlerMethodReturnValueHandler)) &&
                    (((AsyncHandlerMethodReturnValueHandler)handler).isAsyncReturnValue(value, returnType))) {
                return true;
            }
        }
        return false;
    }

    public HandlerMethodReturnValueHandlerComposite addHandler(HandlerMethodReturnValueHandler handler)
    {
        this.returnValueHandlers.add(handler);
        return this;
    }

    public HandlerMethodReturnValueHandlerComposite addHandlers(List<? extends HandlerMethodReturnValueHandler> handlers)
    {
        if (handlers != null) {
            for (HandlerMethodReturnValueHandler handler : handlers) {
                this.returnValueHandlers.add(handler);
            }
        }
        return this;
    }
}