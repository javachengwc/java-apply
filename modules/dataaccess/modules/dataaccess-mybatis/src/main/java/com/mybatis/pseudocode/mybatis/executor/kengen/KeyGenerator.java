package com.mybatis.pseudocode.mybatis.executor.kengen;

import com.mybatis.pseudocode.mybatis.executor.Executor;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;

import java.sql.Statement;


public abstract interface KeyGenerator
{
    public abstract void processBefore(Executor executor, MappedStatement mappedStatement, Statement statement, Object object);

    public abstract void processAfter(Executor executor, MappedStatement mappedStatement, Statement statement, Object object);
}

