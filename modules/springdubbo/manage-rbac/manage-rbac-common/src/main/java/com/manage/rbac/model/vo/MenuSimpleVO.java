package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "菜单简单信息", value = "menuSimpleVO")
public class MenuSimpleVO {

    @ApiModelProperty("菜单id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("系统id")
    private Integer systemId;

    @ApiModelProperty("父菜单id")
    private Integer parentId;

    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("权限")
    private String perms;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("菜单或按钮标识")
    private String tag;

    @ApiModelProperty("类型(0--目录、1--菜单、2--按钮)")
    private Integer type;

    @ApiModelProperty("排序")
    private Integer sort;
}
