package com.component.rest.enums;

//路由结果
public enum RouteResultEnum {
    RouteByMicro(0,"路由微服务"),
    RouteByLocal(1,"路由本地"),
    RouteFail(2,"失败");

    private Integer value;

    private String note;

    RouteResultEnum(Integer value,String note) {
        this.value=value;
        this.note=note;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
