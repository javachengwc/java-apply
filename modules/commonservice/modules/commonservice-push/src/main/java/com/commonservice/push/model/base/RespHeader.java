package com.commonservice.push.model.base;

import org.apache.commons.lang.builder.ToStringBuilder;

public class RespHeader {

    public static Integer SUCCESS=0;

    public static Integer FAIL=-1;

    private String msg;

    private Integer code=SUCCESS;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
