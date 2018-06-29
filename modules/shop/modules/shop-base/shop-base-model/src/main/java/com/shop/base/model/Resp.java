package com.shop.base.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Resp<T> {

    private RespHeader header = new RespHeader();

    private T data;

    public RespHeader getHeader() {
        return header;
    }

    public void setHeader(RespHeader header) {
        this.header = header;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        if(header.getCode() ==RespHeader.SUCCESS.intValue()) {
            return true;
        }
        return false;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}