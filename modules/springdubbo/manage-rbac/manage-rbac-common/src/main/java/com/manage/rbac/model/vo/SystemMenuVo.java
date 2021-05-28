package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "系统菜单信息", value = "systemMenuVo")
@AllArgsConstructor
public class SystemMenuVo {

    @ApiModelProperty("系统")
    private SystemSimpleVo system;

    @ApiModelProperty("菜单列表")
    private List<MenuSimpleVO> menuList;

    public SystemMenuVo() {

    }
}
