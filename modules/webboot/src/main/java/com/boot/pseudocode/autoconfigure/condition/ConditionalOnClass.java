package com.boot.pseudocode.autoconfigure.condition;


import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({OnClassCondition.class})
public @interface ConditionalOnClass
{
    public abstract Class<?>[] value();

    public abstract String[] name();
}
