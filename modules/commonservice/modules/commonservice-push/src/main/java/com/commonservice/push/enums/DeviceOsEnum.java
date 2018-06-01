package com.commonservice.push.enums;

public enum DeviceOsEnum {

    ANDROID("android","安卓",1),

    IPHONE("iphone","苹果",2);

    private String value;

    private String name;

    private Integer flag;

    DeviceOsEnum(String value,String name,Integer flag) {
        this.value=value;
        this.name=name;
        this.flag=flag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
