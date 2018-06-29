package com.shop.book.search.api.enums;

public enum  BookStatuEnum {

    INIT(0,"初始"),
    UP_SHELF(1,"上架"),
    DOWN_SHELF(2,"下架");

    private Integer value;

    private String name;

    BookStatuEnum(Integer value, String name) {
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
