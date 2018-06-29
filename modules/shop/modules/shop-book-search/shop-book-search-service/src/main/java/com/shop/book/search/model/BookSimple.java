package com.shop.book.search.model;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;

public class BookSimple {

    @Field
    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @Field
    @ApiModelProperty(name = "name", value = "书籍名称")
    private String name;

    @Field
    @ApiModelProperty(name = "subTitle", value = "副标题")
    private String subTitle;

    @Field
    @ApiModelProperty(name = "info", value = "简介")
    private String info;

    @Field
    @ApiModelProperty(name = "statu", value = "状态 0--初始 1--上架 2--下架")
    private Integer statu;

    @Field
    @ApiModelProperty(name = "author", value = "作者")
    private String author;

    @Field
    @ApiModelProperty(name = "price", value = "价格，分")
    private Integer price;

    @Field
    @ApiModelProperty(name = "orglPrice", value = "原价，分")
    private Integer orglPrice;

    @Field
    @ApiModelProperty(name = "topType", value = "一级分类")
    private Integer topType;

    @Field
    @ApiModelProperty(name = "topTypeName", value = "一级分类名称")
    private String topTypeName;

    @Field
    @ApiModelProperty(name = "secondType", value = "二级分类")
    private Integer secondType;

    @Field
    @ApiModelProperty(name = "secondTypeName", value = "二级分类名称")
    private String secondTypeName;

    @Field
    @ApiModelProperty(name = "storeId", value = "书店id")
    private Long storeId;

    @Field
    @ApiModelProperty(name = "storeName", value = "书店名称")
    private String storeName;

    @Field
    @ApiModelProperty(name = "publishTime", value = "出版日期")
    private Date publishTime;

    @Field
    @ApiModelProperty(name = "publishTimeStr", value = "出版日期 yyyy-MM-dd")
    private String publishTimeStr;

    @Field
    @ApiModelProperty(name = "publisherId", value = "出版社id")
    private Long publisherId;

    @Field
    @ApiModelProperty(name = "publisherName", value = "出版社名称")
    private String publisherName;

    @Field
    @ApiModelProperty(name = "label", value = "标签")
    private String label;

    @Field
    @ApiModelProperty(name = "saleCnt", value = "销量")
    private Integer saleCnt;

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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getOrglPrice() {
        return orglPrice;
    }

    public void setOrglPrice(Integer orglPrice) {
        this.orglPrice = orglPrice;
    }

    public Integer getTopType() {
        return topType;
    }

    public void setTopType(Integer topType) {
        this.topType = topType;
    }

    public String getTopTypeName() {
        return topTypeName;
    }

    public void setTopTypeName(String topTypeName) {
        this.topTypeName = topTypeName;
    }

    public Integer getSecondType() {
        return secondType;
    }

    public void setSecondType(Integer secondType) {
        this.secondType = secondType;
    }

    public String getSecondTypeName() {
        return secondTypeName;
    }

    public void setSecondTypeName(String secondTypeName) {
        this.secondTypeName = secondTypeName;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishTimeStr() {
        return publishTimeStr;
    }

    public void setPublishTimeStr(String publishTimeStr) {
        this.publishTimeStr = publishTimeStr;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getSaleCnt() {
        return saleCnt;
    }

    public void setSaleCnt(Integer saleCnt) {
        this.saleCnt = saleCnt;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
