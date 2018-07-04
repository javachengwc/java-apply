package com.shop.user.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class Token {

    private Long userId;

    private String deviceToken;

    private String token;

    private Date createTime;

    private Date expireTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
