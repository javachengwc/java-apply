package com.storefront.manage.model.vo;

import org.apache.commons.lang.builder.ToStringBuilder;


public class BrandVo {

    private Long id;

    private String name;

    private String firstIdstryCode;

    private String firstIdstryName;

    private String directIdstryCode;

    private String directIdstryName;

    private Integer isIdstryMark;

    private Long companyId;

    private Integer isJingying;

    private String createTimeStr;

    private String modifiedTimeStr;

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

    public String getFirstIdstryCode() {
        return firstIdstryCode;
    }

    public void setFirstIdstryCode(String firstIdstryCode) {
        this.firstIdstryCode = firstIdstryCode;
    }

    public String getFirstIdstryName() {
        return firstIdstryName;
    }

    public void setFirstIdstryName(String firstIdstryName) {
        this.firstIdstryName = firstIdstryName;
    }

    public String getDirectIdstryCode() {
        return directIdstryCode;
    }

    public void setDirectIdstryCode(String directIdstryCode) {
        this.directIdstryCode = directIdstryCode;
    }

    public String getDirectIdstryName() {
        return directIdstryName;
    }

    public void setDirectIdstryName(String directIdstryName) {
        this.directIdstryName = directIdstryName;
    }

    public Integer getIsIdstryMark() {
        return isIdstryMark;
    }

    public void setIsIdstryMark(Integer isIdstryMark) {
        this.isIdstryMark = isIdstryMark;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getIsJingying() {
        return isJingying;
    }

    public void setIsJingying(Integer isJingying) {
        this.isJingying = isJingying;
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

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
