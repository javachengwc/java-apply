package com.mybatis.pseudocode.mybatis.annotations;

import com.mybatis.pseudocode.mybatis.mapping.JdbcType;
import com.mybatis.pseudocode.mybatis.type.TypeHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Result
{
    public abstract boolean id();

    public abstract String column();

    public abstract String property();

    public abstract Class<?> javaType();

    public abstract JdbcType jdbcType();

    public abstract Class<? extends TypeHandler> typeHandler();

    public abstract One one();

    public abstract Many many();
}
