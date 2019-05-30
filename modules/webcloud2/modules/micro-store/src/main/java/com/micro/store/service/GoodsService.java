package com.micro.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.store.model.pojo.Goods;
import com.micro.store.model.req.GoodsStockReq;
import org.dromara.hmily.annotation.Hmily;

public interface GoodsService extends IService<Goods> {

    @Hmily
    public boolean decreaseStock(GoodsStockReq goodsStockReq);
}
