package com.micro.order.model.third;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "商品库存请求", value = "goodsStockReq")
@Data
public class GoodsStockReq {

    private Long goodsId;

    private Integer count;

    public GoodsStockReq() {

    }

    public GoodsStockReq(Long goodsId,Integer count) {
        this.goodsId=goodsId;
        this.count=count;
    }

}
