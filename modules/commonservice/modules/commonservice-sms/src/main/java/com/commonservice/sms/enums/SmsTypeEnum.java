package com.commonservice.sms.enums;

public enum SmsTypeEnum {

    CAPTCHA(1,"验证码"),

    CONTENT(2,"文本");

    private int value;

    private String name;

    SmsTypeEnum(int value,String name ) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
