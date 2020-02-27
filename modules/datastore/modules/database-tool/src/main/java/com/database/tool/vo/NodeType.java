package com.database.tool.vo;

public enum NodeType {

    DB(1, "DB"), TABLE(2, "TABLE");

    private int value;

    private String desc;

    NodeType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
