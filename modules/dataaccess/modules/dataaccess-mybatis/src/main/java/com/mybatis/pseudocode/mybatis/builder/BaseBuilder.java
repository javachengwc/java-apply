package com.mybatis.pseudocode.mybatis.builder;


import com.mybatis.pseudocode.mybatis.mapping.JdbcType;
import com.mybatis.pseudocode.mybatis.mapping.ParameterMode;
import com.mybatis.pseudocode.mybatis.mapping.ResultSetType;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.type.TypeAliasRegistry;
import com.mybatis.pseudocode.mybatis.type.TypeHandler;
import com.mybatis.pseudocode.mybatis.type.TypeHandlerRegistry;
import org.apache.ibatis.builder.BuilderException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class BaseBuilder
{
    protected final Configuration configuration;

    //别名注册器
    protected final TypeAliasRegistry typeAliasRegistry;

    //数据类型转换注册器
    protected final TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration)
    {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    protected Pattern parseExpression(String regex, String defaultValue) {
        return Pattern.compile(regex == null ? defaultValue : regex);
    }

    protected Boolean booleanValueOf(String value, Boolean defaultValue) {
        return value == null ? defaultValue : Boolean.valueOf(value);
    }

    protected Integer integerValueOf(String value, Integer defaultValue) {
        return value == null ? defaultValue : Integer.valueOf(value);
    }

    protected Set<String> stringSetValueOf(String value, String defaultValue) {
        value = value == null ? defaultValue : value;
        return new HashSet(Arrays.asList(value.split(",")));
    }

    protected JdbcType resolveJdbcType(String alias) {
        if (alias == null)
            return null;
        try
        {
            return JdbcType.valueOf(alias);
        } catch (IllegalArgumentException e) {

            throw new BuilderException("Error resolving JdbcType. Cause: " + e, e);
        }
    }

    protected ResultSetType resolveResultSetType(String alias)
    {
        if (alias == null)
            return null;
        try
        {
            return ResultSetType.valueOf(alias);
        } catch (IllegalArgumentException e) {
            throw new BuilderException("Error resolving ResultSetType. Cause: " + e, e);
        }

    }

    protected ParameterMode resolveParameterMode(String alias)
    {
        if (alias == null)
            return null;
        try
        {
            return ParameterMode.valueOf(alias);
        } catch (IllegalArgumentException e) {
            throw new BuilderException("Error resolving ParameterMode. Cause: " + e, e);
        }
    }

    protected Object createInstance(String alias)
    {
        Class clazz = resolveClass(alias);
        if (clazz == null)
            return null;
        try
        {
            return resolveClass(alias).newInstance();
        } catch (Exception e) {
            throw new BuilderException("Error creating instance. Cause: " + e, e);
        }
    }

    protected Class<?> resolveClass(String alias)
    {
        if (alias == null)
            return null;
        try
        {
            return resolveAlias(alias);
        } catch (Exception e) {
            throw new BuilderException("Error resolving class. Cause: " + e, e);
        }
    }

    protected TypeHandler<?> resolveTypeHandler(Class<?> javaType, String typeHandlerAlias)
    {
        if (typeHandlerAlias == null) {
            return null;
        }
        Class type = resolveClass(typeHandlerAlias);
        if ((type != null) && (!TypeHandler.class.isAssignableFrom(type))) {
            throw new BuilderException("Type " + type.getName() + " is not a valid TypeHandler because it does not implement TypeHandler interface");
        }

        Class typeHandlerType = type;
        return resolveTypeHandler(javaType, typeHandlerType);
    }

    protected TypeHandler<?> resolveTypeHandler(Class<?> javaType, Class<? extends TypeHandler<?>> typeHandlerType) {
        if (typeHandlerType == null) {
            return null;
        }

        TypeHandler handler = this.typeHandlerRegistry.getMappingTypeHandler(typeHandlerType);
        if (handler == null)
        {
            handler = this.typeHandlerRegistry.getInstance(javaType, typeHandlerType);
        }
        return handler;
    }

    protected Class<?> resolveAlias(String alias) {
        return this.typeAliasRegistry.resolveAlias(alias);
    }
}
