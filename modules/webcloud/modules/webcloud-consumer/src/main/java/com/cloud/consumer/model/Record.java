package com.cloud.consumer.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Record {

    private Integer id;

    private String name;

    private String note;

    public Record() {}

    public Record(Integer id,String name,String note) {
        this.id=id;
        this.name=name;
        this.note=note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
