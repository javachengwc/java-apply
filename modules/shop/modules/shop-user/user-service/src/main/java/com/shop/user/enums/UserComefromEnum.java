package com.shop.user.enums;

public enum UserComefromEnum {

    PC(1, "pc"),
    APP(2, "app");

    private int value;

    private String note;

    UserComefromEnum(int value, String note) {
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
