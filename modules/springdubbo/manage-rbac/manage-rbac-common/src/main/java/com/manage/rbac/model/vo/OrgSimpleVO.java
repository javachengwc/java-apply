package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "机构简单信息", value = "orgSimpleVO")
public class OrgSimpleVO {

    @ApiModelProperty("机构id")
    private Integer id;

    @ApiModelProperty("机构名称")
    private String name;
}
