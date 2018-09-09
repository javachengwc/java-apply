package com.mybatis.pseudocode.mybatis.annotations;

import com.mybatis.pseudocode.mybatis.mapping.StatementType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface SelectKey
{
    public abstract String[] statement();

    public abstract String keyProperty();

    public abstract String keyColumn();

    public abstract boolean before();

    public abstract Class<?> resultType();

    public abstract StatementType statementType();
}
