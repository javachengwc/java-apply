package com.storefront.manage.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "roleVo", description = "角色信息")
public class RoleVo implements Serializable {

    @ApiModelProperty("角色ID")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("角色code")
    private String code;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("角色拥有的菜单ID集合")
    private List<Long> menuIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
