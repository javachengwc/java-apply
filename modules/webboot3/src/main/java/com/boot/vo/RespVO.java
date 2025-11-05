package com.boot.vo;

import lombok.Data;

@Data
public class RespVO<T> {

    public static Integer SUCCESS=0;

    public static Integer FAIL=-1;

    private String msg;

    private Integer code=SUCCESS;

    private T data;

    public static <T> RespVO<T> success() {
        return success("");
    }

    public static <T> RespVO<T> success(String msg) {
        RespVO<T> resp = new RespVO<T>();
        resp.setCode(SUCCESS);
        resp.setMsg(msg);
        return resp;
    }

    public static <T> RespVO<T> data(T data) {
        RespVO<T> resp = success();
        resp.setData(data);
        return resp;
    }

    public static <T> RespVO<T> error() {
        return error("");
    }

    public static <T> RespVO<T> error(String msg) {
        RespVO<T> resp = new RespVO<T>();
        resp.setCode(FAIL);
        resp.setMsg(msg);
        return resp;
    }
}
