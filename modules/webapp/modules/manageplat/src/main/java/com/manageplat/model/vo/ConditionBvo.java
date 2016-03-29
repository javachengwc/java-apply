package com.manageplat.model.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class ConditionBvo implements Serializable{

     private ConditionEnum type;

     private String condition;

     private String left;

     private String right;

     public ConditionBvo()
     {

     }

     public ConditionBvo(ConditionEnum type)
     {
         this.type=type;
     }

    public ConditionBvo(ConditionEnum type,String condition)
    {
        this.type=type;
        this.condition=condition;
    }

    public ConditionEnum getType() {
        return type;
    }

    public void setType(ConditionEnum type) {
        this.type = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
