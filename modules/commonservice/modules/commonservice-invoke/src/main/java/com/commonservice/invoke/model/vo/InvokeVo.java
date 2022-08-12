package com.commonservice.invoke.model.vo;

import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 调用信息
 */
@Data
public class InvokeVo implements Serializable {

    @ApiModelProperty("访问接口id")
    private Long resourceId;

    @ApiModelProperty("请求头")
    private Map<String,String> headers;

    @ApiModelProperty("cookie")
    private Map<String,String> cookies;

    @ApiModelProperty("请求参数")
    private Map<String,Object> params;

}
