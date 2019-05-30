package com.micro.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.order.model.pojo.ShopOrder;
import com.micro.order.model.req.OrderReq;

public interface OrderService extends IService<ShopOrder> {

    //下单
    public ShopOrder order(OrderReq orderReq);

    //修改订单状态
    public void changeStatu(Long orderId,Integer curStatu,Integer newStatu);
}
