package com.micro.user.enums;

public enum UserStatuEnum {

    NORMAL(0,"正常"),FORBID(1,"禁用");

    private Integer value;

    private String name;

    private UserStatuEnum(Integer value,String name) {
        this.value=value;
        this.name=name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
