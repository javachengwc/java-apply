package com.spring.pseudocode.webmvc.mvc.method.annotation;

import com.spring.pseudocode.core.core.MethodParameter;
import com.spring.pseudocode.web.web.context.request.ServletWebRequest;
import com.spring.pseudocode.web.web.method.HandlerMethod;
import com.spring.pseudocode.web.web.method.support.HandlerMethodReturnValueHandlerComposite;
import com.spring.pseudocode.web.web.method.support.InvocableHandlerMethod;
import com.spring.pseudocode.web.web.method.support.ModelAndViewContainer;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;

public class ServletInvocableHandlerMethod extends InvocableHandlerMethod
{
    private static final Method CALLABLE_METHOD = ClassUtils.getMethod(Callable.class, "call", new Class[0]);
    private HttpStatus responseStatus;

    private String responseReason;

    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    private MethodParameter returnType;

    public ServletInvocableHandlerMethod(Object handler, Method method)
    {
        super(handler, method);
        initResponseStatus();
    }

    public ServletInvocableHandlerMethod(HandlerMethod handlerMethod)
    {
        super(handlerMethod);
        initResponseStatus();
    }

    private void initResponseStatus() {
        ResponseStatus annotation = (ResponseStatus)getMethodAnnotation(ResponseStatus.class);
        if (annotation != null) {
            this.responseStatus = annotation.code();
            this.responseReason = annotation.reason();
        }
    }

    public void setHandlerMethodReturnValueHandlers(HandlerMethodReturnValueHandlerComposite returnValueHandlers)
    {
        this.returnValueHandlers = returnValueHandlers;
    }

    public MethodParameter getReturnValueType(Object returnValue)
    {
        return this.returnType;
    }

    //调用handle入口
    public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer, Object[] providedArgs) throws Exception
    {
        Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
        setResponseStatus(webRequest);
        //..................................
        try {
            this.returnValueHandlers.handleReturnValue(returnValue, getReturnValueType(returnValue),mavContainer, webRequest);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void setResponseStatus(ServletWebRequest webRequest) throws IOException
    {
        if (this.responseStatus == null) {
            return;
        }
        if (StringUtils.hasText(this.responseReason)) {
            webRequest.getResponse().sendError(this.responseStatus.value(), this.responseReason);
        }
        else {
            webRequest.getResponse().setStatus(this.responseStatus.value());
        }

        webRequest.getRequest().setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, this.responseStatus);
    }

    private boolean hasResponseStatus()
    {
        return this.responseStatus != null;
    }

    public ServletInvocableHandlerMethod wrapConcurrentResult(Object result)
    {
        return new ConcurrentResultHandlerMethod(result, new ConcurrentResultMethodParameter(result));
    }

    private class ConcurrentResultMethodParameter extends HandlerMethod.HandlerMethodParameter
    {
        private final Object returnValue;
        private ResolvableType returnType;

        public ConcurrentResultMethodParameter(Object returnValue)
        {
            super(-1);
            this.returnValue = returnValue;
            //this.returnType = ResolvableType.forType(super.getGenericParameterType()).getGeneric(new int[] { 0 });
        }

        public Class<?> getParameterType()
        {
            if (this.returnValue != null) {
                return this.returnValue.getClass();
            }
            Class parameterType = super.getParameterType();
            if ((ResponseBodyEmitter.class.isAssignableFrom(parameterType)) || (StreamingResponseBody.class.isAssignableFrom(parameterType)))
            {
                return parameterType;
            }
            if (ResolvableType.NONE.equals(this.returnType))
            {
                throw new IllegalArgumentException("Expected one of Callable, DeferredResult, or ListenableFuture: " + super.getParameterType());
            }
            return this.returnType.getRawClass();
        }

        public Type getGenericParameterType()
        {
            return this.returnType.getType();
        }
    }

    private class ConcurrentResultHandlerMethod extends ServletInvocableHandlerMethod
    {
        private final MethodParameter returnType;

        public ConcurrentResultHandlerMethod(Object result, ServletInvocableHandlerMethod.ConcurrentResultMethodParameter returnType)
        {
            //super(ServletInvocableHandlerMethod.CALLABLE_METHOD);
            super(null);
            setHandlerMethodReturnValueHandlers(ServletInvocableHandlerMethod.this.returnValueHandlers);
            this.returnType = returnType;
        }

        public Class<?> getBeanType()
        {
            return ServletInvocableHandlerMethod.this.getBeanType();
        }

        public MethodParameter getReturnValueType(Object returnValue)
        {
            return this.returnType;
        }

        public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType)
        {
            return ServletInvocableHandlerMethod.this.getMethodAnnotation(annotationType);
        }
    }
}