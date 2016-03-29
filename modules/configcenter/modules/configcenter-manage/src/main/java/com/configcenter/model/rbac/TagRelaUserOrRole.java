package com.configcenter.model.rbac;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 用户或则角色关联的标签
 */
public class TagRelaUserOrRole {

    private Integer id;

    //标签
    private String tag;

    //关联的用户或角色id
    private Integer relaId;

    //归属用户或角色 0---用户  1---角色
    private Integer belong;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getRelaId() {
        return relaId;
    }

    public void setRelaId(Integer relaId) {
        this.relaId = relaId;
    }

    public Integer getBelong() {
        return belong;
    }

    public void setBelong(Integer belong) {
        this.belong = belong;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
