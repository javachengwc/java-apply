package com.shop.book.enums;

public enum AdvertStatuEnum {

    INIT(0, "初始"),
    UP(1, "上线"),
    DOWN(2,"下线");

    private int value;

    private String note;

    AdvertStatuEnum(int value, String note) {
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
