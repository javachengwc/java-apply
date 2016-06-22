package com.ocean.parser;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * SQL路由上下文
 */
public class RouteContext {

    private Set<SqlTable> tables = new LinkedHashSet<SqlTable>();

    private SqlBuilder sqlBuilder;

    public Set<SqlTable> getTables() {
        return tables;
    }

    public void setTables(Set<SqlTable> tables) {
        this.tables = tables;
    }

    public SqlBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    public void setSqlBuilder(SqlBuilder sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }
}
