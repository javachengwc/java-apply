package com.micro.order.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(description = "订单信息", value = "orderVo")
@Data
public class OrderVo {

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "orderSn", value = "订单sn")
    private String orderSn;

    @ApiModelProperty(value = "买家id")
    private Long buyerId;

    private String buyerName;

    private String buyerMobile;

    private Integer statu;

    private Long goodsId;

    private String goodsName;

    private Integer goodsCount;

    private Long totalAmount;

    private Date createTime;

}
