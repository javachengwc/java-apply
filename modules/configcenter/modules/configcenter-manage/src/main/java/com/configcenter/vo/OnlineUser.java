package com.configcenter.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 在线的用户
 */
public class OnlineUser {

    private Integer id;

    //账号
    private String account;

    //名称
    private String name;

    //登陆时间
    private int loginTime;

    //最近一次心跳时间
    private int lastedHeartbeat;

    //登陆次数
    private int loginCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(int loginTime) {
        this.loginTime = loginTime;
    }

    public int getLastedHeartbeat() {
        return lastedHeartbeat;
    }

    public void setLastedHeartbeat(int lastedHeartbeat) {
        this.lastedHeartbeat = lastedHeartbeat;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
