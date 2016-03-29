package com.configcenter.model.rbac;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 角色
 */
public class Role{

    private Integer id;

    private String name;

    //中文名
    private String nameCh;

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

    public String getNameCh() {
        return nameCh;
    }

    public void setNameCh(String nameCh) {
        this.nameCh = nameCh;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
