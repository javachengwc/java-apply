package com.micro.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.store.dao.mapper.GoodsMapper;
import com.micro.store.model.pojo.Goods;
import com.micro.store.model.req.GoodsStockReq;
import com.micro.store.service.GoodsService;
import org.dromara.hmily.annotation.Hmily;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private static Logger logger= LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Hmily(confirmMethod = "confirmDec", cancelMethod = "cancelDec")
    public boolean decreaseStock(GoodsStockReq goodsStockReq) {
        logger.info("GoodsServiceImpl decreaseStock start,goodsStockReq={}",goodsStockReq);
        return true;
    }

    public boolean confirmDec(GoodsStockReq goodsStockReq) {
        logger.info("GoodsServiceImpl confirmDec start,goodsStockReq={}",goodsStockReq);
        return true;
    }

    public boolean cancelDec(GoodsStockReq goodsStockReq) {
        logger.info("GoodsServiceImpl cancelDec start,goodsStockReq={}",goodsStockReq);
        return true;
    }
}
