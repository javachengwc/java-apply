package com.shop.order.service;

import com.shop.base.model.Resp;
import com.shop.order.model.pojo.ShopOrder;

public interface OrderExtendService {

    //取消订单
    public Resp<Integer> cancelOrder(Long orderId);

    public Boolean innerCancelOrder(ShopOrder shopOrder);
}
