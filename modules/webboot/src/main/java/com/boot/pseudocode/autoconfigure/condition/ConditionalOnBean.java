package com.boot.pseudocode.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({OnBeanCondition.class})
public @interface ConditionalOnBean
{
    public abstract Class<?>[] value();

    public abstract String[] type();

    public abstract Class<? extends Annotation>[] annotation();

    public abstract String[] name();

    public abstract SearchStrategy search();
}
