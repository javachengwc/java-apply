package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(description = "菜单树节点简单信息", value = "menuNodeSimpleVO")
public class MenuNodeSimpleVO {

    @ApiModelProperty("菜单id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("父菜单id")
    private Integer parentId;

    @ApiModelProperty("子菜单集合")
    private List<MenuNodeSimpleVO> children = new ArrayList<MenuNodeSimpleVO>();
}
