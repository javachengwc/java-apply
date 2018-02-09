package com.shop.order.api.enums;

public enum OrderActionEnum {

    CREATE(1,"创建订单"),
    PAY(2,"支付订单"),
    DELIVER(3,"发货订单"),
    CANCEl(4,"取消订单");

    private Integer value;

    private String name;

    OrderActionEnum(Integer value,String name) {
        this.value=value;
        this.name=name;
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
