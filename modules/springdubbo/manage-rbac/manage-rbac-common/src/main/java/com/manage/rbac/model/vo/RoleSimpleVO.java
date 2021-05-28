package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel(description = "角色简单信息", value = "roleSimpleVO")
@AllArgsConstructor
public class RoleSimpleVO {

    @ApiModelProperty("角色id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    public RoleSimpleVO() {

    }

}
