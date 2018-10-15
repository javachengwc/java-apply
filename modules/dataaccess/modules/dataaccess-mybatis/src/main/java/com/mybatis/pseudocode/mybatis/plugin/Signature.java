package com.mybatis.pseudocode.mybatis.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Signature
{
    public abstract Class<?> type();

    public abstract String method();

    public abstract Class<?>[] args();
}
