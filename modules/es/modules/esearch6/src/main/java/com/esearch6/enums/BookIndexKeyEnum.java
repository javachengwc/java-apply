package com.esearch6.enums;

public enum BookIndexKeyEnum {

    ID("id", "ID"),
    NAME("name", "书名"),
    STATU("statu", "状态"),
    PRICE("price", "价格"),
    TOP_TYPE("topType", "一级分类"),
    TOP_TYPE_NAME("topTypeName","一级分类名称"),
    SECOND_TYPE("secondType", "二级分类"),
    SECOND_TYPE_NAME("secondTypeName","二级分类名称"),
    STORE_ID("storeId", "书店Id"),
    STORE_NAME("storeName", "书店名称"),
    LABEL("label", "标签"),
    PUBLISHER_ID("publisherId", "出版社Id"),
    PUBLISHER_NAME("publisherNAME", "出版社名称");

    private String indexKey;

    private String name;

    BookIndexKeyEnum(String indexKey, String name) {
        this.indexKey = indexKey;
        this.name = name;
    }

    public String getIndexKey() {
        return indexKey;
    }

    public void setIndexKey(String indexKey) {
        this.indexKey = indexKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
