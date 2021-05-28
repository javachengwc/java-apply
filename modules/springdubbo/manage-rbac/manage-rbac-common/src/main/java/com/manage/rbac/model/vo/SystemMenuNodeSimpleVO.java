package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "系统菜单树简单节点信息", value = "systemMenuSimpleVO")
public class SystemMenuNodeSimpleVO {

    @ApiModelProperty("系统")
    private SystemSimpleVo system;

    @ApiModelProperty("菜单列表")
    private List<MenuNodeSimpleVO> menuList;
}
