package com.mybatis.pseudocode.mybatis.scripting;

import com.mybatis.pseudocode.mybatis.executor.parameter.ParameterHandler;
import com.mybatis.pseudocode.mybatis.mapping.BoundSql;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;
import com.mybatis.pseudocode.mybatis.mapping.SqlSource;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import org.apache.ibatis.parsing.XNode;

//LanguageDriver主要作用于解析select|update|insert|delete节点为完整的SQL语句
public abstract interface LanguageDriver
{
    public abstract ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object object, BoundSql boundSql);

    public abstract SqlSource createSqlSource(Configuration configuration, XNode xNode, Class<?> paramClass);

    public abstract SqlSource createSqlSource(Configuration configuration, String param, Class<?> paramClass);
}
