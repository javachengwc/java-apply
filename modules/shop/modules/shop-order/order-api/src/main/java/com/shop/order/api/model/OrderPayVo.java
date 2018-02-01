package com.shop.order.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(description = "订单支付单信息", value = "orderPayVo")
public class OrderPayVo {

    @ApiModelProperty(name = "orderNo", value = "订单编号")
    private String orderNo;

    @ApiModelProperty(name = "orderPayNo", value = "订单支付编号")
    private String orderPayNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderPayNo() {
        return orderPayNo;
    }

    public void setOrderPayNo(String orderPayNo) {
        this.orderPayNo = orderPayNo;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
