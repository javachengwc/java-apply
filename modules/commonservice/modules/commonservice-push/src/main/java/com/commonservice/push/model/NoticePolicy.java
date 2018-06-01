package com.commonservice.push.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "noticePolicy", description = "消息发送策略")
public class NoticePolicy {

    @JsonProperty("start_time")
    @ApiModelProperty(name = "startTime",value = "可选，格式:yyyy-MM-dd HH:mm:ss,若不填写表示立即发送。")
    private String startTime;

    @JsonProperty("expire_time")
    @ApiModelProperty(name = "expireTime",value = "可选，消息过期时间,格式同startTime,如不填，默认为3天后过期。")
    private String expireTime;

    @JsonProperty("max_send_num")
    @ApiModelProperty(name = "maxSendNum",value = "可选，发送限速，每秒发送的最大条数。最小值1000")
    private Integer maxSendNum;

    @JsonProperty("out_biz_no")
    @ApiModelProperty(name = "outBizNo",value = "可选，开发者对消息的唯一标识，" +
            "服务器会根据这个标识避免重复发送out_biz_no只对任务类消息生效。")
    private String outBizNo;

    @JsonProperty("apns_collapse_id")
    @ApiModelProperty(name = "apnsCollapseId",value = "可选,推给ios用," +
            "多条相同apns_collapse_id的消息，ios仅展示最新的一条")
    private String apnsCollapseId;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getMaxSendNum() {
        return maxSendNum;
    }

    public void setMaxSendNum(Integer maxSendNum) {
        this.maxSendNum = maxSendNum;
    }

    public String getOutBizNo() {
        return outBizNo;
    }

    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo;
    }

    public String getApnsCollapseId() {
        return apnsCollapseId;
    }

    public void setApnsCollapseId(String apnsCollapseId) {
        this.apnsCollapseId = apnsCollapseId;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
