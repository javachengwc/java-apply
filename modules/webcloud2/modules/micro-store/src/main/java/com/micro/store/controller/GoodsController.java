package com.micro.store.controller;

import com.micro.store.model.pojo.Goods;
import com.micro.store.model.vo.GoodsVo;
import com.micro.store.service.GoodsService;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.base.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api("商品接口")
@RequestMapping("/goods")
@RestController
public class GoodsController {

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
}
