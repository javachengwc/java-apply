package com.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "resp", description = "响应")
@Data
public class Resp<T> {

    @ApiModelProperty("响应头")
    private RespHeader header = new RespHeader();

    @ApiModelProperty("响应数据")
    private T data;

    public Resp() {

    }

    public Resp(T data) {
        this.data=data;
    }

    public RespHeader getHeader() {
        return header;
    }

    public boolean isSuccess() {
        if(header.getCode() ==RespHeader.SUCCESS.intValue()) {
            return true;
        }
        return false;
    }

    public static <T> Resp<T> error() {
        return Resp.error("");
    }

    public static <T> Resp<T> error(String msg) {
        return Resp.error(RespHeader.FAIL,msg);
    }

    public static <T> Resp<T> error(Resp<?> bizResp) {
        Integer code =bizResp.getHeader().getCode();
        String msg =bizResp.getHeader().getMsg();
        return Resp.error(code,msg);
    }

    public static <T> Resp<T> error(Integer code,String msg) {
        Resp<T> resp = new Resp<T>();
        resp.getHeader().setCode(code);
        resp.getHeader().setMsg(msg);
        return resp;
    }

    public static <T> Resp<T> error(IResultCode resultCode) {
        return Resp.error(resultCode.getCode(),resultCode.getMsg());
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
        resp.getHeader().setMsg(msg);
        resp.setData(data);
        return resp;
    }
}