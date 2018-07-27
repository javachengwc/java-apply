package com.spring.pseudocode.web.web.method;

import com.spring.pseudocode.beans.factory.BeanFactory;
import com.spring.pseudocode.core.core.MethodParameter;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class HandlerMethod {

    private final Object bean;
    private final BeanFactory beanFactory;
    private final Class<?> beanType;
    private final Method method;
    private final Method bridgedMethod;
    private final MethodParameter[] parameters;
    private final HandlerMethod resolvedFromHandlerMethod;

    public HandlerMethod(Object bean, Method method)
    {
        this.bean = bean;
        this.beanFactory = null;
        this.beanType = ClassUtils.getUserClass(bean);
        this.method = method;
        this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
        this.parameters = initMethodParameters();
        this.resolvedFromHandlerMethod = null;
    }

    public HandlerMethod(Object bean, String methodName, Class<?>[] parameterTypes)
            throws NoSuchMethodException
    {
        this.bean = bean;
        this.beanFactory = null;
        this.beanType = ClassUtils.getUserClass(bean);
        this.method = bean.getClass().getMethod(methodName, parameterTypes);
        this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(this.method);
        this.parameters = initMethodParameters();
        this.resolvedFromHandlerMethod = null;
    }

    public HandlerMethod(String beanName, BeanFactory beanFactory, Method method)
    {
        this.bean = beanName;
        this.beanFactory = beanFactory;
        this.beanType = ClassUtils.getUserClass(beanFactory.getType(beanName));
        this.method = method;
        this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
        this.parameters = initMethodParameters();
        this.resolvedFromHandlerMethod = null;
    }

    protected HandlerMethod(HandlerMethod handlerMethod)
    {
        this.bean = handlerMethod.bean;
        this.beanFactory = handlerMethod.beanFactory;
        this.beanType = handlerMethod.beanType;
        this.method = handlerMethod.method;
        this.bridgedMethod = handlerMethod.bridgedMethod;
        this.parameters = handlerMethod.parameters;
        this.resolvedFromHandlerMethod = handlerMethod.resolvedFromHandlerMethod;
    }

    private HandlerMethod(HandlerMethod handlerMethod, Object handler)
    {
        this.bean = handler;
        this.beanFactory = handlerMethod.beanFactory;
        this.beanType = handlerMethod.beanType;
        this.method = handlerMethod.method;
        this.bridgedMethod = handlerMethod.bridgedMethod;
        this.parameters = handlerMethod.parameters;
        this.resolvedFromHandlerMethod = handlerMethod;
    }

    private MethodParameter[] initMethodParameters()
    {
        int count = this.bridgedMethod.getParameterTypes().length;
        MethodParameter[] result = new MethodParameter[count];
//        for (int i = 0; i < count; i++) {
//            result[i] = new HandlerMethodParameter(i);
//        }
        return result;
    }

    public Object getBean()
    {
        return this.bean;
    }

    public Method getMethod()
    {
        return this.method;
    }

    public Class<?> getBeanType()
    {
        return this.beanType;
    }

    protected Method getBridgedMethod()
    {
        return this.bridgedMethod;
    }

    public MethodParameter[] getMethodParameters()
    {
        return this.parameters;
    }

    public HandlerMethod getResolvedFromHandlerMethod()
    {
        return this.resolvedFromHandlerMethod;
    }

    public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType)
    {
        return AnnotatedElementUtils.findMergedAnnotation(this.method, annotationType);
    }

    public HandlerMethod createWithResolvedBean()
    {
        Object handler = this.bean;
        if ((this.bean instanceof String)) {
            String beanName = (String)this.bean;
            handler = this.beanFactory.getBean(beanName);
        }
        return new HandlerMethod(this, handler);
    }

    public boolean isVoid()
    {
        return Void.TYPE.equals(getReturnType().getParameterType());
    }

    public MethodParameter getReturnType()
    {
        return new HandlerMethodParameter(-1);
    }

    public boolean equals(Object other)
    {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HandlerMethod)) {
            return false;
        }
        HandlerMethod otherMethod = (HandlerMethod)other;
        return (this.bean.equals(otherMethod.bean)) && (this.method.equals(otherMethod.method));
    }

    public int hashCode()
    {
        return this.bean.hashCode() * 31 + this.method.hashCode();
    }

    public String toString()
    {
        return this.method.toGenericString();
    }

    protected class HandlerMethodParameter extends MethodParameter
    {
        public HandlerMethodParameter(int index)
        {
            super(method,index);
        }

        public Class<?> getContainingClass()
        {
            return HandlerMethod.this.getBeanType();
        }

        public <T extends Annotation> T getMethodAnnotation(Class<T> annotationType)
        {
            return HandlerMethod.this.getMethodAnnotation(annotationType);
        }
    }
}
