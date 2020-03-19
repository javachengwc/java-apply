package com.cache.springredis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "dataVo", value = "dataVo")
public class DataVo implements Serializable  {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;
}
