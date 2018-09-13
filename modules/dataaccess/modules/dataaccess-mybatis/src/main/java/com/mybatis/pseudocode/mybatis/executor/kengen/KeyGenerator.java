package com.mybatis.pseudocode.mybatis.executor.kengen;

import com.mybatis.pseudocode.mybatis.executor.Executor;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;

import java.sql.Statement;

public abstract interface KeyGenerator
{
    //在statementHandler的数据库操作准备parameter时执行processBefore
    public abstract void processBefore(Executor executor, MappedStatement mappedStatement, Statement statement, Object object);

    //在statementHandler的数据库操作update之后执行processAfter
    public abstract void processAfter(Executor executor, MappedStatement mappedStatement, Statement statement, Object object);
}

