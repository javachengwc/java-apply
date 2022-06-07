package com.sharding.bootdemo.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

@ApiModel(description = "订单信息", value = "orderVo")
@Data
public class OrderVo implements Serializable {

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

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
