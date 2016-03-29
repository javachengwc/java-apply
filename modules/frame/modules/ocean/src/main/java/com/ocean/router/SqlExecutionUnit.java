package com.ocean.router;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * sql执行单元
 */
public class SqlExecutionUnit {

    private String dataSource;

    private String sql;

    public SqlExecutionUnit(String dataSource, String sql) {
        this.dataSource = dataSource;
        this.sql = sql;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
