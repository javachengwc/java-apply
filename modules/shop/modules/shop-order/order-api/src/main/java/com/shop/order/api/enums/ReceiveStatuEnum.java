package com.shop.order.api.enums;

public enum ReceiveStatuEnum {

    NOT_RECEIVE(0,"未收货"),
    RECEIVED(1,"已收货");

    private Integer value;

    private String name;

    ReceiveStatuEnum(Integer value,String name) {
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
