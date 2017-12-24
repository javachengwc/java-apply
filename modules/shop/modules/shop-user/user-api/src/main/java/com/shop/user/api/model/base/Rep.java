package com.shop.user.api.model.base;

import org.apache.commons.lang.builder.ToStringBuilder;

public class  Rep<T> {

    private RepHeader header = new RepHeader();

    private T data;

    public RepHeader getHeader() {
        return header;
    }

    public void setHeader(RepHeader header) {
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