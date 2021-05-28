package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户组简单信息", value = "crowdSimpleVO")
public class CrowdSimpleVO {

    @ApiModelProperty("用户组id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;
}
