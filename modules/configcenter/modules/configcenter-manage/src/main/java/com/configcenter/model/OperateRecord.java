package com.configcenter.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

/**
 * 操作记录
 */
public class OperateRecord {

    private Integer id;

    private Date operateTime;

    private Integer operatorId;

    private String operatorName;

    private String operation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
