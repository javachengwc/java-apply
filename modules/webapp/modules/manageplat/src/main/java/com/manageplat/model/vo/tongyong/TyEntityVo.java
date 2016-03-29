package com.manageplat.model.vo.tongyong;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 通用列表实体项
 * ty_entity
 */
public class TyEntityVo {

    private Integer id;

    //名称
    private String name;

    //表名，如果查询就是查某张表的话，可录入表名
    private String tableName;

    //结果集查询语句
    private String querySql;

    //分页查询页数
    private Integer pageSize=20;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
