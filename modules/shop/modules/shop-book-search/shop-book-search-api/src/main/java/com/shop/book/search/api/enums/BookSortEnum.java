package com.shop.book.search.api.enums;

import org.apache.commons.lang.StringUtils;

public enum BookSortEnum {

    LASTED_SHELF("A","最新上架","shelf_time",OrderBySortEnum.DESC),
    PRICE_LOWEST("B","价格最低","price",OrderBySortEnum.ASC),
    PRICE_HIGHEST("C","价格最高","price",OrderBySortEnum.DESC);

    private String value;

    private String name;

    private String orderByColumn;

    private OrderBySortEnum orderBySort;

    BookSortEnum(String value, String name, String orderByColumn, OrderBySortEnum orderBySort ) {
        this.value=value;
        this.name=name;
        this.orderByColumn=orderByColumn;
        this.orderBySort= orderBySort;
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

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public OrderBySortEnum getOrderBySort() {
        return orderBySort;
    }

    public void setOrderBySort(OrderBySortEnum orderBySort) {
        this.orderBySort = orderBySort;
    }

    public  static BookSortEnum  getSortEnumByValue(String sortValue) {
        if(StringUtils.isBlank(sortValue)) {
            return null;
        }
        for(BookSortEnum per:BookSortEnum.values()) {
            if(sortValue.equalsIgnoreCase(per.getValue())) {
                return per;
            }
        }
        return null;
    }
}
