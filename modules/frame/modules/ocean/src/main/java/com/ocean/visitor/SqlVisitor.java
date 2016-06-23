package com.ocean.visitor;

import com.ocean.shard.DatabaseType;
import com.ocean.parser.ParseContext;
import com.ocean.parser.SqlBuilder;

/**
 * SQL解析基础访问器接口
 */
public interface SqlVisitor {

    /**
     * 获取数据库类型
     */
    public DatabaseType getDatabaseType();

    /**
     * 获取解析上下文对象
     */
    public ParseContext getParseContext();

    /**
     * 获取SQL构建器
     */
    public SqlBuilder getSqlBuilder();

    /**
     * 打印替换标记
     */
    public void printToken(String token);
}
