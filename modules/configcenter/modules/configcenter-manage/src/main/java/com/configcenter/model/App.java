package com.configcenter.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 应用
 */
public class App {

    private Integer id;

    private String name;

    //中文名
    private String nameCh;

    private String note;

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

    public String getNameCh() {
        return nameCh;
    }

    public void setNameCh(String nameCh) {
        this.nameCh = nameCh;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
