package com.sharding.bootdemo.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(description = "订单详单项信息", value = "orderItemVo")
public class OrderItemVo {

    @ApiModelProperty(name = "orderItemId", value = "详单项Id")
    private Long orderItemId;

    @ApiModelProperty(name = "orderId", value = "订单id")
    private Long orderId;

    @ApiModelProperty(name = "userId", value = "用户id")
    private Long userId;

    @ApiModelProperty(name = "userId", value = "用户名")
    private String userName;

    @ApiModelProperty(name = "itemPrice", value = "详单项总价")
    private Long itemPrice;

    @ApiModelProperty(name = "shopId", value = "店铺id")
    private Long shopId;

    @ApiModelProperty(name = "shopName", value = "店铺名称")
    private String shopName;

    @ApiModelProperty(name = "productId", value = "产品id")
    private Long productId;

    @ApiModelProperty(name = "productName", value = "产品名称")
    private String productName;

    @ApiModelProperty(name = "skuNum", value = "sku编码")
    private String skuNum;

    @ApiModelProperty(name = "skuName", value = "sku编码名称")
    private String skuName;

    @ApiModelProperty(name = "count", value = "数量")
    private  Integer count;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

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

    public Long getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Long itemPrice) {
        this.itemPrice = itemPrice;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(String skuNum) {
        this.skuNum = skuNum;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
