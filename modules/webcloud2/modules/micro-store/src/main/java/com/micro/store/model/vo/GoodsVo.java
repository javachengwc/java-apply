package com.micro.store.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(description = "商品信息", value = "goodsVo")
@Data
public class GoodsVo {

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "name", value = "商品名称")
    private String name;

    @ApiModelProperty("单价")
    private Long price;

    @ApiModelProperty("库存")
    private Integer stockCount;

    @ApiModelProperty("锁定库存")
    private Integer lockStockCnt;

    private Date createTime;

}
