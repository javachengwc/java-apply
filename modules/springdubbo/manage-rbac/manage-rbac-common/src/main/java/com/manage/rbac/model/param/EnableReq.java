package com.manage.rbac.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "启用或禁用参数", value = "enableReq")
public class EnableReq {

    @ApiModelProperty("实体id")
    private Integer id;

    @ApiModelProperty("启用或禁用 0--启用 1--禁用")
    private Integer operate;
}
