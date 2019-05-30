package com.micro.order.enums;

public enum OrderStatuEnum {

    INIT(1, "待支付"),

    PAYING(2, "支付中"),

    PAY_FAIL(3, "支付失败"),

    COMPLETE(4, "完成");

    private int value;

    private String name;

    OrderStatuEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
