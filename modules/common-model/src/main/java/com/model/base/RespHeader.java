package com.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "respHeader", description = "响应头")
@Data
public class RespHeader {

    public static Integer SUCCESS=0;

    public static Integer FAIL=-1;

    @ApiModelProperty("响应信息")
    private String msg;

    @ApiModelProperty("响应code")
    private Integer code=SUCCESS;

}
