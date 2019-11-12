package com.micro.store.controller;

import com.micro.store.model.pojo.Goods;
import com.micro.store.model.req.GoodsStockReq;
import com.micro.store.model.vo.GoodsVo;
import com.micro.store.service.GoodsService;
import com.model.base.Req;
import com.model.base.Resp;
import com.model.base.RespHeader;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api("商品接口")
@RequestMapping("/goods")
@RestController
public class GoodsController {

    private static Logger logger= LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsService goodsService;

    @ApiOperation(value = "根据id查询商品", notes = "根据id查询商品")
    @RequestMapping(value = "/queryGoodsById", method = RequestMethod.POST)
    public Resp<GoodsVo> queryGoodsById(@RequestBody Req<Long> req) {
        Resp<GoodsVo> resp = new Resp<GoodsVo>();
        Long goodsId = req.getData();
        if(goodsId==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        Goods goods  = goodsService.getById(goodsId);
        if(goods==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("无数据");
            return resp;
        }
        GoodsVo goodsVo = TransUtil.transEntity(goods,GoodsVo.class);
        resp.setData(goodsVo);
        return resp;
    }

    @ApiOperation(value = "减少商品库存", notes = "减少商品库存")
    @RequestMapping(value = "/decreaseStock", method = RequestMethod.POST)
    public Resp<Void> decreaseStock(@RequestBody Req<GoodsStockReq> req) {
        Resp<Void> resp = new Resp<Void>();
        GoodsStockReq goodsStockReq = req.getData();
        Long goodsId = goodsStockReq==null?null:goodsStockReq.getGoodsId();
        Integer count = goodsStockReq==null?null:goodsStockReq.getCount();
        logger.info("GoodsController decreaseStock start,goodsId={},count={}",goodsId,count);
        if(goodsId==null || count==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        goodsService.decreaseStock(goodsStockReq);
        return resp;
    }
}
