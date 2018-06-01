package com.commonservice.sms.enums;

public enum SendResultEnum {

    INIT(0,"初始"),

    SUCCESS(1,"成功"),

    FAIL(2,"失败");

    private Integer value;

    private String name;

    SendResultEnum(Integer value,String name) {
        this.value=value;
        this.name =name;
    }

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
