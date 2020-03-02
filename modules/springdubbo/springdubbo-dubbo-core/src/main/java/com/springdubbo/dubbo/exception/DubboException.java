package com.springdubbo.dubbo.exception;

/**
 * dubbo异常类
 */
public class DubboException extends RuntimeException {

    private static final long serialVersionUID = -22234342342L;

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DubboException() {

    }

    public DubboException(Integer code, String msg) {
        this.code= code;
        this.msg= msg;
    }

}
