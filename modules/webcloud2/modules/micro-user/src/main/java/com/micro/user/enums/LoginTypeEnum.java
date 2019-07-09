package com.micro.user.enums;

public enum LoginTypeEnum {

    ACCOUNT(0,"账号密码"),MOBILE_CODE(1,"手机验证码"),THIRD_ACCOUNT(2,"第三方账号");

    private Integer type;

    private String name;

    private LoginTypeEnum(Integer type,String name) {
        this.type=type;
        this.name=name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static LoginTypeEnum getLoginType(Integer value) {
        if(value==null) {
            return null;
        }
        for(LoginTypeEnum per:LoginTypeEnum.values()) {
            if(value== per.getType().intValue()) {
                return per;
            }
        }
        return null;
    }
}
