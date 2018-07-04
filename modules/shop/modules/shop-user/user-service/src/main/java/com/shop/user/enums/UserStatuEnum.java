package com.shop.user.enums;

public enum UserStatuEnum {

    NORMAL(0, "正常"),
    BANNED(1, "禁用");

    private int value;

    private String note;

    UserStatuEnum(int value, String note) {
        this.value = value;
        this.note = note;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
