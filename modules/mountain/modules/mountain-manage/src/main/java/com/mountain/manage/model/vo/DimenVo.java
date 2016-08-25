package com.mountain.manage.model.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 维度vo
 */
public class DimenVo {

    //名称
    private String name;

    //类型  1--应用 2--服务 3--机器
    private Integer type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
