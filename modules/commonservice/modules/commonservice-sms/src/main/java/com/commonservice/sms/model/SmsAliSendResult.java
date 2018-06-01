package com.commonservice.sms.model;

import com.commonservice.sms.enums.SmsAliResultCodeEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SmsAliSendResult {

    //请求ID
    private String requestId;

    //状态码-返回OK代表请求成功
    private String code;

    //状态码的描述
    private String message;

    //发送回执ID,可根据该ID查询具体的发送状态
    private String bizId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public boolean isSuccess() {
        if(StringUtils.isBlank(code)) {
            return false;
        }
        if(code.equalsIgnoreCase(SmsAliResultCodeEnum.OK.getValue())) {
            return true;
        }
        return false;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
