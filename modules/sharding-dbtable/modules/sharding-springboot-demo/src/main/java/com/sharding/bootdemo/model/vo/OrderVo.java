package com.sharding.bootdemo.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

@ApiModel(description = "订单信息", value = "orderVo")
public class OrderVo {

    @ApiModelProperty(name = "orderId", value = "订单Id")
    private Long orderId;

    @ApiModelProperty(name = "userId", value = "用户Id")
    private Long userId;

    @ApiModelProperty(name = "userName", value = "用户名称")
    private String userName;

    @ApiModelProperty(name = "statu", value = "状态")
    private Integer statu;

    @ApiModelProperty(name = "price", value = "订单总价")
    private Long price;

    @ApiModelProperty(name = "shopId", value = "店铺id")
    private Long shopId;

    @ApiModelProperty(name = "shopName", value = "店铺名称")
    private String shopName;

    @ApiModelProperty(name = "orderItemList", value = "订单详单项列表")
    private List<OrderItemVo> orderItemList;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<OrderItemVo> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemVo> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
