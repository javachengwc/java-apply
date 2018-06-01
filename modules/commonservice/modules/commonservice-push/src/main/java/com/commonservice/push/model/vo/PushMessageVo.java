package com.commonservice.push.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "pushMessageVo", description = "推送消息")
public class PushMessageVo {

    @ApiModelProperty(name = "reqId",value = "业务请求Id")
    private Integer reqId;

    @ApiModelProperty(name = "comeFrom",value = "进入来源")
    private Integer comeFrom;

    @ApiModelProperty(name = "title",value = "标题")
    private String title;

    @ApiModelProperty(name = "message",value = "内容")
    private String message;

    @ApiModelProperty(name = "deviceToken",value = "手机设备token")
    private String deviceToken;

    @ApiModelProperty(name = "deviceOs",value = "手机设备系统,参见DeviceOsEnum")
    private String deviceOs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    public Integer getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(Integer comeFrom) {
        this.comeFrom = comeFrom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
