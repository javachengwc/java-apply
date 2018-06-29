package com.shop.book.search.api.enums;

public enum OrderBySortEnum {

    ASC("asc","升序"),
    DESC("desc","降序");

    private String value;

    private String name;

    OrderBySortEnum(String value, String name) {
        this.value=value;
        this.name=name;
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
}
