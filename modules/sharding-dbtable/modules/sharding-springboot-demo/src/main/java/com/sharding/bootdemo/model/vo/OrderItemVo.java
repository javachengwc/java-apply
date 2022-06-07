package com.sharding.bootdemo.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

@ApiModel(description = "订单详单项信息", value = "orderItemVo")
@Data
public class OrderItemVo implements Serializable {

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

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
