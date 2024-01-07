package com.otd.boot.web.base.model.vo;

import lombok.Data;

@Data
public class Resp<T> {

    public static Integer SUCCESS=0;

    public static Integer FAIL=-1;

    private String msg;

    private Integer code=SUCCESS;

    private T data;

    public Resp() {

    }

    public Resp(T data) {
        this.data=data;
    }

    public boolean isSuccess() {
        if(code ==SUCCESS.intValue()) {
            return true;
        }
        return false;
    }

    public static <T> Resp<T> error() {
        return Resp.error("");
    }

    public static <T> Resp<T> error(String msg) {
        return Resp.error(FAIL,msg);
    }

    public static <T> Resp<T> error(Resp<?> bizResp) {
        Integer code =bizResp.getCode();
        String msg =bizResp.getMsg();
        return Resp.error(code,msg);
    }

    public static <T> Resp<T> error(Integer code,String msg) {
        Resp<T> resp = new Resp<T>();
        resp.setCode(code);
        resp.setMsg(msg);
        return resp;
    }

    public static <T> Resp<T> success() {
        return success("");
    }

    public static <T> Resp<T> success(String msg) {
        return  success(null,msg);
    }

    public static <T> Resp<T> data(T data) {
        return  success(data,null);
    }

    public static <T> Resp<T> success(T data,String msg) {
        Resp<T> resp = new Resp<T>();
        resp.setMsg(msg);
        resp.setData(data);
        return resp;
    }
}
