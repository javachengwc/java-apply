package com.manage.rbac.model.enums;

//启用禁用
public enum EnableEnum {

    ENABLE(0,"启用"),
    FORBID(1,"禁用");

    private int value;

    private String name;

    EnableEnum(int value,String name) {
        this.value=value;
        this.name=name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static EnableEnum getByValue(Integer vlu) {
        if(vlu==null) {
            return null;
        }
        for(EnableEnum per: EnableEnum.values()) {
            if(per.getValue()==vlu) {
                return per;
            }
        }
        return null;
    }

}
