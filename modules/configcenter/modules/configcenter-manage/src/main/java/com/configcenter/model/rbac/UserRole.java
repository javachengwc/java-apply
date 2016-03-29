package com.configcenter.model.rbac;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 用户角色
 */
public class UserRole {

    private Integer userId;

    private Integer roleId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
