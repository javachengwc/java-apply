package com.springdubbo.common;

import com.model.base.IResultCode;

public enum RestCode implements IResultCode {

    SUCCESS(0, "成功"),
    DEAL_EXCEPTION(-1, "处理异常"),
    V_PARAMS_IS_NULL(-2, "参数为空"),
    V_PARAM_ERROR(-3, "参数错误"),
    V_NOT_LOGIN(-4, "未登录");

    private Integer code;

    private String msg;

    private RestCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
