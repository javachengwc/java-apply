package com.app.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "user_act_note")
public class UserActNote implements Serializable {

    private static final long serialVersionUID = -1687689122;

    @Id
    @Column(name = "Id")
    private Integer  id;

    @Column(name = "name")
    private String name;

    @Column(name = "name_ch")
    private String nameCh;

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

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
