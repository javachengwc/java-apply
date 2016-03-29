package com.rule.data.handler;


import org.apache.commons.lang.builder.ToStringBuilder;

public class BaseRespInfo {

    private String code = "0";

    private String description = "Succeed";

    public BaseRespInfo() {
    }

    public BaseRespInfo(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
