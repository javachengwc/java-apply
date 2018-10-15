package com.mybatis.pseudocode.mybatis.plugin;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Intercepts
{
    public abstract Signature[] value();
}