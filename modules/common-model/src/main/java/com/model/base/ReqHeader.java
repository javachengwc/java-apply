package com.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "reqHeader", description = "请求头")
@Data
public class ReqHeader {

    @ApiModelProperty(name = "app", value = "app应用名称")
    private String app;

    @ApiModelProperty(name = "appVersion", value = "app应用版本")
    private String appVersion;

    @ApiModelProperty(name = "token", value = "登录会话token")
    private String token;

    @ApiModelProperty(name = "deviceOs", value = "手机设备系统")
    private String deviceOs;

    @ApiModelProperty(name = "deviceOsVersion", value = "手机设备系统版本")
    private String deviceOsVersion;

    @ApiModelProperty(name = "deviceToken", value = "手机设备的唯一标识")
    private String deviceToken;
}
