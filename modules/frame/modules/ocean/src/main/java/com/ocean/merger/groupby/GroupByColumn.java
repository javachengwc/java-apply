package com.ocean.merger.groupby;

import com.ocean.merger.orderby.OrderByColumn;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 分组列对象
 */
public class GroupByColumn {

    private String name;

    private String alias;

    private OrderByColumn.OrderByType orderByType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public OrderByColumn.OrderByType getOrderByType() {
        return orderByType;
    }

    public void setOrderByType(OrderByColumn.OrderByType orderByType) {
        this.orderByType = orderByType;
    }

    public GroupByColumn()
    {

    }

    public GroupByColumn(String name,String alias,OrderByColumn.OrderByType orderByType)
    {
        this.name=name;
        this.alias=alias;
        this.orderByType=orderByType;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
