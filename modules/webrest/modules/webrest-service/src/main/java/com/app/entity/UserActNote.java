package com.app.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "user_act_note")
@ApiModel(value = "用户行为记录", description = "user_act_note")
public class UserActNote implements Serializable {

    private static final long serialVersionUID = -1687689122;

    @Id
    @Column(name = "id")
    @ApiModelProperty(name = "id", value = "id")
    private Integer  id;

    @Column(name = "name")
    @ApiModelProperty(name = "nameCh", value = "行为名称")
    private String name;

    @Column(name = "name_ch")
    @ApiModelProperty(name = "nameCh", value = "行为中文名称")
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
