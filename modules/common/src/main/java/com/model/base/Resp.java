package com.model.base;

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

    public static <T> Resp<T> error(String msg) {
        Resp<T> resp = new Resp<T>();
        resp.getHeader().setCode(RespHeader.FAIL);
        resp.getHeader().setMsg(msg);
        return resp;
    }

    public static <T> Resp<T> error(Resp<?> bizResp) {
        Resp<T> resp = new Resp<T>();
        resp.getHeader().setCode(bizResp.getHeader().getCode());
        resp.getHeader().setMsg(bizResp.getHeader().getMsg());
        return resp;
    }

    public static <T> Resp<T> error(Integer code,String msg) {
        Resp<T> resp = new Resp<T>();
        resp.getHeader().setCode(code);
        resp.getHeader().setMsg(msg);
        return resp;
    }

    public static <T> Resp<T> success(T data) {
        return  success(data,null);
    }

    public static <T> Resp<T> success(T data,String msg) {
        Resp<T> resp = new Resp<T>();
        resp.getHeader().setMsg(msg);
        resp.setData(data);
        return resp;
    }
}