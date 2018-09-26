package com.spring.pseudocode.context.context.annotation;

import org.springframework.core.type.AnnotatedTypeMetadata;

public abstract interface Condition
{
    public abstract boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata);
}
