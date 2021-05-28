package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "实体", value = "entityVO")
public class EntityVO implements Serializable {

    @ApiModelProperty("实体id")
    private Integer id;
}