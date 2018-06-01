package com.commonservice.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {

    //验证码每日发送的最大短信数
    private Integer maxSendCount;

    //验证码最大校验次数
    private Integer maxVerifyCount;

    //验证码过期时长,分钟
    private Integer captchaTimeout;

    //阿里云sms
    private ThirdSms ali;

    //第三方sms
    public static class ThirdSms {

        private String accessId;

        private String accessKey;

        private String product;

        private String domain;

        public ThirdSms() { }

        public String getAccessId() {
            return accessId;
        }

        public void setAccessId(String accessId) {
            this.accessId = accessId;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }

    public Integer getMaxSendCount() {
        return maxSendCount;
    }

    public void setMaxSendCount(Integer maxSendCount) {
        this.maxSendCount = maxSendCount;
    }

    public Integer getMaxVerifyCount() {
        return maxVerifyCount;
    }

    public void setMaxVerifyCount(Integer maxVerifyCount) {
        this.maxVerifyCount = maxVerifyCount;
    }

    public Integer getCaptchaTimeout() {
        return captchaTimeout;
    }

    public void setCaptchaTimeout(Integer captchaTimeout) {
        this.captchaTimeout = captchaTimeout;
    }

    public ThirdSms getAli() {
        return ali;
    }

    public void setAli(ThirdSms ali) {
        this.ali = ali;
    }
}
