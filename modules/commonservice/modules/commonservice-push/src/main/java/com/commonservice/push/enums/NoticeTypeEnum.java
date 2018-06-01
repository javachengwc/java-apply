package com.commonservice.push.enums;

public enum NoticeTypeEnum {

    MESSAGE("message","消息"),

    NOTIFICATION("notification","通知");

    private String value;

    private String name;

    NoticeTypeEnum(String value,String name) {
        this.value=value;
        this.name=name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
