package com.mybatis.pseudocode.mybatis.mapping;

public abstract interface SqlSource
{
    public abstract BoundSql getBoundSql(Object paramObject);
}
