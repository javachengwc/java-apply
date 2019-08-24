package com.micro.basic.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "cityVo", description = "城市")
public class CityVo {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("城市code")
    private String code;

    @ApiModelProperty("城市名称")
    private String name;

    @ApiModelProperty("省份code")
    private String parentCode;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}
