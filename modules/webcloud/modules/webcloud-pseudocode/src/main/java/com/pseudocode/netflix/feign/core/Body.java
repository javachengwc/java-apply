package com.pseudocode.netflix.feign.core;

import java.lang.annotation.*;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Body
{
    public abstract String value();
}