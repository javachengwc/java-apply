package com.component.rest.springmvc.util;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SimpleOrderVo {

    private Long id;

    private String orderNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
