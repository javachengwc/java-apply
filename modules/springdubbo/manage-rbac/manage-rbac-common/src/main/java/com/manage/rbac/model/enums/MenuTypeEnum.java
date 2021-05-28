package com.manage.rbac.model.enums;

public enum MenuTypeEnum {

    CATALOG(0,"目录"),
    MENU(1,"菜单"),
    BUTTON(2,"按钮");

    private int value;

    private String name;

    MenuTypeEnum(int value,String name) {
        this.value=value;
        this.name=name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static MenuTypeEnum getMenuTypeByValue(Integer vlu) {
        if(vlu==null) {
            return null;
        }
        for(MenuTypeEnum per: MenuTypeEnum.values()) {
            if(per.getValue()==vlu) {
                return per;
            }
        }
        return null;
    }
}
