package com.manage.rbac.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "角色菜单参数", value = "roleMenuReq")
public class RoleMenuReq {

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty("菜单列表")
    private List<Integer> menuList;
}
