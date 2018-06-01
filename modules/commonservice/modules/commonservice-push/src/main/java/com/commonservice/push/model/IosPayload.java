package com.commonservice.push.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "iosPayload", description = "ios通知消息载体")
public class IosPayload implements NoticePayload {

    @ApiModelProperty(name = "aps",required = true,value = "APNs定义数据")
    private IosAps aps;

    @JsonProperty("reqId")
    private Integer reqId;

    @JsonProperty("comeFrom")
    private Integer comeFrom;

    public IosAps getAps() {
        return aps;
    }

    public void setAps(IosAps aps) {
        this.aps = aps;
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

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
