package com.storefront.manage.model.vo;

import com.shop.base.model.PageParam;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

public class UserQueryVo extends PageParam {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("状态 -1--全部 0--正常，1--冻结")
    private Integer status;

    @ApiModelProperty("角色Id")
    private Long roleId;

    @ApiModelProperty("开始时间 格式:yyyy-MM-dd")
    private String beginDate;

    @ApiModelProperty("结束时间 格式:yyyy-MM-dd")
    private String endDate;

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
