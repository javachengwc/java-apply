package com.micro.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.store.dao.mapper.GoodsMapper;
import com.micro.store.model.pojo.Goods;
import com.micro.store.model.req.GoodsStockReq;
import com.micro.store.service.GoodsService;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Hmily(confirmMethod = "confirmDec", cancelMethod = "cancelDec")
    public boolean decreaseStock(GoodsStockReq goodsStockReq) {
        return true;
    }

    public boolean confirmDec(GoodsStockReq userWalletReq) {
        return true;
    }

    public boolean cancelDec(GoodsStockReq userWalletReq) {
        return true;
    }
}
