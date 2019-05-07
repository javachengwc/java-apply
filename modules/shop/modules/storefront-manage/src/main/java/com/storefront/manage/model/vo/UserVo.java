package com.storefront.manage.model.vo;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class UserVo {

    private Long id;

    private String name;

    private String mobile;

    private Integer status;

    private String createTimeStr;

    private String modifiedTimeStr;

    private String roleNames;

    @ApiModelProperty("拥有的角色Id集合")
    private List<Long> roleIds = new ArrayList<Long>();

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getModifiedTimeStr() {
        return modifiedTimeStr;
    }

    public void setModifiedTimeStr(String modifiedTimeStr) {
        this.modifiedTimeStr = modifiedTimeStr;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
