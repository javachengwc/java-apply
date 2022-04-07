package com.manageplat.model.vo.web;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BaseResponse {

    protected Integer error;
    protected String  msg;

    public BaseResponse() {

    }

    public BaseResponse(Integer error) {
        this.error = error;
    }

    public BaseResponse(Integer error, String msg) {
        this.error = error;
        this.msg   = msg;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
