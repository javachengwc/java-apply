package com.mybatis.pseudocode.mybatis.builder.annotation;

import java.lang.reflect.Method;

public class MethodResolver
{
    private final MapperAnnotationBuilder annotationBuilder;
    private final Method method;

    public MethodResolver(MapperAnnotationBuilder annotationBuilder, Method method)
    {
        this.annotationBuilder = annotationBuilder;
        this.method = method;
    }

    public void resolve() {
        this.annotationBuilder.parseStatement(this.method);
    }
}
