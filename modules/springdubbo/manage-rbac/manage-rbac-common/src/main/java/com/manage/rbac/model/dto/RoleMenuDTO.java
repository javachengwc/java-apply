package com.manage.rbac.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleMenuDTO extends BaseDTO {

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty("菜单列表")
    private List<Integer> menuList;
}
