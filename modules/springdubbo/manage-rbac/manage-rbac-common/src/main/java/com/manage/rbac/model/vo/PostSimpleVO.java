package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "岗位简单信息", value = "postSimpleVO")
public class PostSimpleVO {

    @ApiModelProperty("岗位id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

}
