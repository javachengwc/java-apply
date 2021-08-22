package com.mybatis.pseudocode.mybatis.plugin;

import java.lang.annotation.*;

//拦截器注解
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Intercepts
{
    public abstract Signature[] value();
}