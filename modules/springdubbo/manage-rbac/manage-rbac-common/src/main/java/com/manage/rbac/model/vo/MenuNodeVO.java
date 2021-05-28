package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(description = "菜单树节点", value = "menuNodeVO")
public class MenuNodeVO {

    @ApiModelProperty("菜单id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("状态，0:正常、1:禁用，默认值:0")
    private Integer state;

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

    @ApiModelProperty("是否已选择，只在分配角色菜单才用此节点 0--否 1--是")
    private Integer selected;

    @ApiModelProperty("子菜单集合")
    private List<MenuNodeVO> children = new ArrayList<MenuNodeVO>();
}
