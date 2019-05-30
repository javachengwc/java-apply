package com.micro.store.model.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "商品库存请求", value = "goodsStockReq")
@Data
public class GoodsStockReq {

    private Long goodsId;

    private Integer count;

}
