package com.shop.user.api.model.base;

import org.apache.commons.lang.builder.ToStringBuilder;

public class RepHeader {

    public static Integer FAIL=0;

    public static Integer SUCCESS=1;

    //1--成功 0--失败
    private Integer rt;

    private String msg;

    private Integer code;

    public Integer getRt() {
        return rt;
    }

    public void setRt(Integer rt) {
        this.rt = rt;
    }

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
