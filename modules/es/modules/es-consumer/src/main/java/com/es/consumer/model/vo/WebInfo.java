package com.es.consumer.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "webInfo",description = "web信息")
public class WebInfo implements Serializable {

    @ApiModelProperty("contextPath")
    private String contextPath;

    @ApiModelProperty("requestURI")
    private String requestURI;

    @ApiModelProperty("requestURL")
    private String requestURL;

    @ApiModelProperty("servletPath")
    private String servletPath;

    @ApiModelProperty("webappRootPath")
    private String webappRootPath;

}
