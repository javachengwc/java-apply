package com.shop.user.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

@ApiModel(description = "用户信息", value = "userInfo")
public class UserInfo {

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "name", value = "姓名")
    private String name;

    @ApiModelProperty(name = "nickName", value = "昵称")
    private String nickName;

    @ApiModelProperty(name = "sex", value = "性别 0--女 1--男")
    private Integer sex;

    @ApiModelProperty(name = "mobile", value = "手机号")
    private String mobile;

    @ApiModelProperty(name = "statu", value = "状态 0--正常 1--禁用")
    private Integer statu;

    @ApiModelProperty(name = "regTime", value = "注册时间")
    private Date regTime;

    @ApiModelProperty(name = "regTimeStr", value = "注册时间  yyyy-MM-dd HH:mm:ss")
    private String regTimeStr;

    @ApiModelProperty(name = "lastLoginTime", value = "最后登陆时间")
    private Date lastLoginTime;

    @ApiModelProperty(name = "lastLoginTimeStr", value = "最后登陆时间 yyyy-MM-dd HH:mm:ss")
    private String lastLoginTimeStr;

    @ApiModelProperty(name = "token", value = "登录token,只有登录接口才会返回值")
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getRegTimeStr() {
        return regTimeStr;
    }

    public void setRegTimeStr(String regTimeStr) {
        this.regTimeStr = regTimeStr;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginTimeStr() {
        return lastLoginTimeStr;
    }

    public void setLastLoginTimeStr(String lastLoginTimeStr) {
        this.lastLoginTimeStr = lastLoginTimeStr;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
