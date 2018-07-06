package com.shop.book.api.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "advertVo", description = "广告信息")
public class AdvertVo {

    @ApiModelProperty( name = "id",value = "广告id")
    private Long id;

    @ApiModelProperty( name = "title",value = "标题")
    private String title;

    @ApiModelProperty( name = "content",value = "内容")
    private String content;

    @ApiModelProperty( name = "statu",value = "状态 0--初始 1--上线 2--下线")
    private Integer statu;

    @ApiModelProperty( name = "positionCode",value = "广告位code")
    private String positionCode;

    @ApiModelProperty( name = "positionName",value = "广告位名称")
    private String positionName;

    @ApiModelProperty( name = "imgUrl",value = "广告图片")
    private String imgUrl;

    @ApiModelProperty( name = "sort",value = "排序")
    private Integer sort;

    @ApiModelProperty( name = "goType",value = "广告跳转类型 0--url  1--跳搜索页")
    private Integer goType;

    @ApiModelProperty( name = "goValue",value = "跳转参数,json格式")
    private String goValue;

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

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
