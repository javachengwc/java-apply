package com.shop.order.api.enums;

public enum PayChannelEnum {

    NIL(0,"无"),
    ZFB(1,"支付宝"),
    WX(2,"微信"),
    ONLINE_BANK(3,"网银");

    private Integer value;

    private String name;

    PayChannelEnum(Integer value,String name) {
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
