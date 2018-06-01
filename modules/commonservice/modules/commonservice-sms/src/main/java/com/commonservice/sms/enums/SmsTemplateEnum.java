package com.commonservice.sms.enums;

import org.apache.commons.lang.StringUtils;

//短信模板枚举
public enum SmsTemplateEnum {

    //此处需要用真实的短信模板id代替
    CAPTCHA("SMS_xxxxxxxx","你的验证码是${code}");

    private String code;

    private String content;

    SmsTemplateEnum(String code,String content) {
        this.code=code;
        this.content=content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static  SmsTemplateEnum getTemplateEnumByCode(String code) {
        if(StringUtils.isBlank(code)) {
            return null;
        }
        for(SmsTemplateEnum per:SmsTemplateEnum.values()) {
            if(per.getCode().equalsIgnoreCase(code)) {
                return per;
            }
        }
        return null;
    }
}
