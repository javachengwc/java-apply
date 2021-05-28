package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "系统简单信息", value = "systemSimpleVo")
public class SystemSimpleVo {

    @ApiModelProperty("系统id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;
}
