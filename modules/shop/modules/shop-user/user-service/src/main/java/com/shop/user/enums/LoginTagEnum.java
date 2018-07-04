package com.shop.user.enums;

public enum LoginTagEnum {

    LOGIN(0, "登录"),
    LOGOUT(1, "登出");

    private int value;

    private String note;

    LoginTagEnum(int value, String note) {
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
