package com.configcenter.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

/**
 * 配置项
 */
public class ConfigItem {

    private Integer id;

    //应用id
    private Integer appId;

    //配置项key
    private String key;

    //配置项value
    private String value;

    //更新时间
    private Date updateAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
