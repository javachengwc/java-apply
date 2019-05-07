package com.storefront.manage.model.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

public class StoreVo {

    private Long id;

    private String name;

    private String info;

    private Integer isBusinessMark;

    private String firstIdstryCode;

    private String firstIdstryName;

    private String directIdstryCode;

    private String directIdstryName;

    private Integer isJingying;

    private Integer isJiameng;

    private Long brandId;

    private String brandName;

    private String startTimeStr;

    private String endTimeStr;

    private String provinceCode;

    private String provinceName;

    private String cityCode;

    private String cityName;

    private String areaCode;

    private String areaName;

    private String positionName;

    private Integer positionType;

    private String streetName;

    private String detailAddress;

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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getIsBusinessMark() {
        return isBusinessMark;
    }

    public void setIsBusinessMark(Integer isBusinessMark) {
        this.isBusinessMark = isBusinessMark;
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

    public Integer getIsJingying() {
        return isJingying;
    }

    public void setIsJingying(Integer isJingying) {
        this.isJingying = isJingying;
    }

    public Integer getIsJiameng() {
        return isJiameng;
    }

    public void setIsJiameng(Integer isJiameng) {
        this.isJiameng = isJiameng;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Integer getPositionType() {
        return positionType;
    }

    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
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
