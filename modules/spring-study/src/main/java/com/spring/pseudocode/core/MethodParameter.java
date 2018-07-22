package com.spring.pseudocode.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Map;

public class MethodParameter
{
    private final Method method;
    private final Constructor<?> constructor;
    private final int parameterIndex;
    private int nestingLevel = 1;
    Map<Integer, Integer> typeIndexesPerLevel;
    private volatile Class<?> containingClass;
    private volatile Class<?> parameterType;
    private volatile Type genericParameterType;
    private volatile Annotation[] parameterAnnotations;
    private volatile String parameterName;

    public MethodParameter(Method method, int parameterIndex)
    {
        this(method, parameterIndex, 1);
    }

    public MethodParameter(Method method, int parameterIndex, int nestingLevel)
    {
        this.method = method;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
        this.constructor = null;
    }

    public MethodParameter(Constructor<?> constructor, int parameterIndex)
    {
        this(constructor, parameterIndex, 1);
    }

    public MethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel)
    {
        this.constructor = constructor;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
        this.method = null;
    }

    public MethodParameter(MethodParameter original)
    {
        this.method = original.method;
        this.constructor = original.constructor;
        this.parameterIndex = original.parameterIndex;
        this.nestingLevel = original.nestingLevel;
        this.typeIndexesPerLevel = original.typeIndexesPerLevel;
        this.containingClass = original.containingClass;
        this.parameterType = original.parameterType;
        this.genericParameterType = original.genericParameterType;
        this.parameterAnnotations = original.parameterAnnotations;
        this.parameterName = original.parameterName;
    }

    public Method getMethod()
    {
        return this.method;
    }

    public Constructor<?> getConstructor()
    {
        return this.constructor;
    }

    public Member getMember()
    {
        if (this.method != null) {
            return this.method;
        }

        return this.constructor;
    }

    public AnnotatedElement getAnnotatedElement()
    {
        if (this.method != null) {
            return this.method;
        }

        return this.constructor;
    }

    public Class<?> getDeclaringClass()
    {
        return getMember().getDeclaringClass();
    }
}
