package com.mybatis.pseudocode.mybatis.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//标明拦截器拦截的接口、方法以及对应的参数类型
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Signature
{
    public abstract Class<?> type();

    public abstract String method();

    public abstract Class<?>[] args();
}
