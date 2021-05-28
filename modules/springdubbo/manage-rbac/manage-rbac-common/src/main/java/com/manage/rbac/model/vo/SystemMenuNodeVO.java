package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "系统菜单树节点", value = "systemMenuNodeVo")
@AllArgsConstructor
public class SystemMenuNodeVO {

    @ApiModelProperty("系统")
    private SystemSimpleVo system;

    @ApiModelProperty("菜单列表")
    private List<MenuNodeVO> menuList;

    public SystemMenuNodeVO() {

    }
}
