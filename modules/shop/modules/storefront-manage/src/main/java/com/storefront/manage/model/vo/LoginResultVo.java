package com.storefront.manage.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

@ApiModel("登录结果")
public class LoginResultVo {

    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("登录会话token")
    private String token;

    @ApiModelProperty("拥有的菜单")
    private List<MenuVo> menus;

    @ApiModelProperty("拥有的角色")
    private List<RoleVo> roles;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<MenuVo> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuVo> menus) {
        this.menus = menus;
    }

    public List<RoleVo> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleVo> roles) {
        this.roles = roles;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
