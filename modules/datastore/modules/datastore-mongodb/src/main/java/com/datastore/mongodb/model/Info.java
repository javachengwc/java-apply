package com.datastore.mongodb.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Info {

    private String no;

    private String name;

    private String data;

    private Integer cnt;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
