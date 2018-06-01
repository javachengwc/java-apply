package com.commonservice.push.model.base;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class Req<T> implements Serializable {

    private ReqHeader header;

    private T data;
    public ReqHeader getHeader() {
        return header;
    }

    public void setHeader(ReqHeader header) {
        this.header = header;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

