package com.shop.book.manage.model.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

public class AdvertVo {

    private Long id;

    private String title;

    private String content;

    private Integer statu;

    private String positionCode;

    private String positionName;

    private String imgUrl;

    private Integer sort;

    private Integer goType;

    private String goValue;

    private String startTimeStr;

    private String endTimeStr;

    private String createTimeStr;

    private String modifiedTimeStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getGoType() {
        return goType;
    }

    public void setGoType(Integer goType) {
        this.goType = goType;
    }

    public String getGoValue() {
        return goValue;
    }

    public void setGoValue(String goValue) {
        this.goValue = goValue;
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
