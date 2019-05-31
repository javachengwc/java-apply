package com.micro.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.store.model.pojo.Goods;
import com.micro.store.model.req.GoodsStockReq;
import org.dromara.hmily.annotation.Hmily;

public interface GoodsService extends IService<Goods> {

    //@Hmily,这里不用加注解@Hmily,具体实现方法中加此注解即可
    public boolean decreaseStock(GoodsStockReq goodsStockReq);
}
