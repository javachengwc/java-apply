package com.commonservice.sms.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "smsVo", description = "短信信息")
public class SmsVo {

    @ApiModelProperty( name = "smsId",value = "smsId")
    private Long smsId;

    @ApiModelProperty( name = "mobile",value = "手机号")
    private String mobile;

    @ApiModelProperty( name = "content",value = "内容")
    private String content;

    public Long getSmsId() {
        return smsId;
    }

    public void setSmsId(Long smsId) {
        this.smsId = smsId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
