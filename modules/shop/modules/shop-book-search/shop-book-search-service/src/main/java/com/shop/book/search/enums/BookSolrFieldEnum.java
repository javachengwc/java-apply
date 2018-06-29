package com.shop.book.search.enums;

public enum BookSolrFieldEnum {

    KEYWORD("keyword","关键字"),
    STATU("statu","状态"),
    PRICE("price","价格");

    private String field;

    private String name;

    BookSolrFieldEnum(String field,String name) {
        this.field=field;
        this.name=name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
