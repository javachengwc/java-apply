package com.shop.order.api.enums;

public enum DeliverStatuEnum {

    NOT_DELIVER(0,"未发货"),
    DONE(1,"已发货");

    private Integer value;

    private String name;

    DeliverStatuEnum(Integer value,String name) {
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
