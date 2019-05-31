package com.micro.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.store.dao.ext.GoodsDao;
import com.micro.store.dao.mapper.GoodsMapper;
import com.micro.store.dao.plus.GoodsPlusMapper;
import com.micro.store.model.pojo.Goods;
import com.micro.store.model.req.GoodsStockReq;
import com.micro.store.service.GoodsService;
import org.dromara.hmily.annotation.Hmily;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsPlusMapper, Goods> implements GoodsService {

    private static Logger logger= LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsMapper goodsMapper;

    @Hmily(confirmMethod = "confirmDec", cancelMethod = "cancelDec")
    public boolean decreaseStock(GoodsStockReq goodsStockReq) {
        Long goodsId = goodsStockReq.getGoodsId();
        logger.info("GoodsServiceImpl decreaseStock start,goodsId={},goodsStockReq={}",goodsId,goodsStockReq);
        goodsDao.decreaseStock(goodsId,goodsStockReq.getCount());
        Goods goods =this.getById(goodsId);
        logger.info("GoodsServiceImpl decreaseStock 操作后商品库存及锁定库存为,stockCount={},lockStockCnt={}",
                goods.getStockCount(),goods.getLockStockCnt());
        return true;
    }

    public boolean confirmDec(GoodsStockReq goodsStockReq) {
        Long goodsId = goodsStockReq.getGoodsId();
        logger.info("GoodsServiceImpl confirmDec start,goodsId={},goodsStockReq={}",goodsId,goodsStockReq);
        goodsDao.decLockStock(goodsId,goodsStockReq.getCount());
        Goods goods =this.getById(goodsId);
        logger.info("GoodsServiceImpl confirmDec 操作后商品库存及锁定库存为,stockCount={},lockStockCnt={}",
                goods.getStockCount(),goods.getLockStockCnt());
        return true;
    }

    public boolean cancelDec(GoodsStockReq goodsStockReq) {
        Long goodsId = goodsStockReq.getGoodsId();
        logger.info("GoodsServiceImpl cancelDec start,goodsId={},goodsStockReq={}",goodsId,goodsStockReq);
        goodsDao.backStock(goodsId,goodsStockReq.getCount());
        Goods goods =this.getById(goodsId);
        logger.info("GoodsServiceImpl cancelDec 操作后商品库存及锁定库存为,stockCount={},lockStockCnt={}",
                goods.getStockCount(),goods.getLockStockCnt());
        return true;
    }
}
