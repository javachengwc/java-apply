package com.shop.order.api.enums;

public enum PayStateEnum {

    INIT(0,"未支付"),
    DONE(1,"已支付"),
    FAIL(2,"支付失败");

    private Integer value;

    private String name;

    PayStateEnum(Integer value,String name) {
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
