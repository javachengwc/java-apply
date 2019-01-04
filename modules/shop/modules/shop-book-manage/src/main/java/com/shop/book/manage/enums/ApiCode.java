package com.shop.book.manage.enums;

public enum ApiCode {

    SUCCESS(0, "成功"),
    PARAM_FAIL(10001, "参数校验不通过"),
    NO_LOGIN(10002, "未登录或登录已失效"),
    ACCOUNT_UNKOWN(10003,"账号不正确"),
    CAPTAL_FAIL(10004,"验证码不正确"),
    ACCOUNT_LOCKED(10005,"账号冻结"),
    LOGIN_OVER_LIMIT(10006,"登录超过次数"),
    UNAUTH(10007,"无权限操作"),
    NO_DATA(10008, "信息不存在"),
    UNKOWN_FAIL(99999,"未知错误");

    private Integer code;

    private String message;

    private ApiCode(Integer code,String message) {
        this.code=code;
        this.message=message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
