package com.mybatis.pseudocode.mybatis.annotations;

import com.mybatis.pseudocode.mybatis.mapping.ResultSetType;
import com.mybatis.pseudocode.mybatis.mapping.StatementType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface Options
{
    public abstract boolean useCache();

    public abstract FlushCachePolicy flushCache();

    public abstract ResultSetType resultSetType();

    public abstract StatementType statementType();

    public abstract int fetchSize();

    public abstract int timeout();

    public abstract boolean useGeneratedKeys();

    public abstract String keyProperty();

    public abstract String keyColumn();

    public abstract String resultSets();

    public static enum FlushCachePolicy
    {
        DEFAULT,

        TRUE,

        FALSE;
    }
}
