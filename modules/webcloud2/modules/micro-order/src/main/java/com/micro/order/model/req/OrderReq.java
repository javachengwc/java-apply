package com.micro.order.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "下单请求model", value = "orderReq")
@Data
public class OrderReq {

    @ApiModelProperty("买家id")
    private Long buyerId;

    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("商品数量")
    private Integer goodsCount;

    @ApiModelProperty("金额")
    private Long totalAmount;

}
