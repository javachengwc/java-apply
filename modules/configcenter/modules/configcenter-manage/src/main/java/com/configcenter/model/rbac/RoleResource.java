package com.configcenter.model.rbac;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 角色资源
 */
public class RoleResource {

    private Integer roleId;

    private Integer resourceId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
