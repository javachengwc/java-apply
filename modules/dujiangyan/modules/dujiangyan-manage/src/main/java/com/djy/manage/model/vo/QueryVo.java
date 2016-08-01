package com.djy.manage.model.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 查询vo
 */
public class QueryVo implements Serializable{

    //查询维度,machine--机器维度,service--服务维度,application--应用维度
    private String dimenType;

    //指定的维度值
    private String assignValue;

    //查询值
    private String queryValue;

    //状态 0--所有, 1--启用, 2--禁用或屏蔽
    private Integer state;

    private Integer page;

    private Integer start;

    private Integer rows;

    public String getDimenType() {
        return dimenType;
    }

    public void setDimenType(String dimenType) {
        this.dimenType = dimenType;
    }

    public String getAssignValue() {
        return assignValue;
    }

    public void setAssignValue(String assignValue) {
        this.assignValue = assignValue;
    }

    public String getQueryValue() {
        return queryValue;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public void genPage()
    {
        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 20;
        }
        if(start==null) {
            start = (page - 1) * rows;
        }
        if (start < 0) {
            start = 0;
        }
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
