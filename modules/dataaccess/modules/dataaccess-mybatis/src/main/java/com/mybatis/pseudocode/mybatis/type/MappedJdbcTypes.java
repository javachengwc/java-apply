package com.mybatis.pseudocode.mybatis.type;

import com.mybatis.pseudocode.mybatis.mapping.JdbcType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface MappedJdbcTypes
{
    public abstract JdbcType[] value();

    public abstract boolean includeNullJdbcType();
}