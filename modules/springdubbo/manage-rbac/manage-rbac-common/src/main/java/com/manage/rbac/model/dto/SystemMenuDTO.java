package com.manage.rbac.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class SystemMenuDTO implements Serializable {

    @ApiModelProperty("系统")
    private SystemDTO system;

    @ApiModelProperty("菜单列表")
    private List<MenuDTO> menuList;

    public SystemMenuDTO() {

    }
}
