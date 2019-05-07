package com.storefront.manage.enums;

public enum UserStatuEnum {

    NOMAL(0,"正常"),
    FREEZE(1,"冻结");

    private UserStatuEnum(Integer value,String name) {
        this.value=value;
        this.name=name;
    }

    private Integer value;

    private String name;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
