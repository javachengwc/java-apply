package com.micro.order.service.remote;

import com.micro.order.model.third.GoodsStockReq;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "micro-store")
public interface GoodsService {

    //扣减库存
    @Hmily
    @RequestMapping("/goods/decreaseStock")
    Resp<Void> decreaseStock(@RequestBody Req<GoodsStockReq> req);

}
