package com.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "reqHeader", description = "请求头信息")
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

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getDeviceOsVersion() {
        return deviceOsVersion;
    }

    public void setDeviceOsVersion(String deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
