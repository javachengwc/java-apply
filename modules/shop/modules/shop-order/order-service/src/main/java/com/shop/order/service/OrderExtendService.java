package com.shop.order.service;

import com.shop.order.api.model.base.Rep;

public interface OrderExtendService {

    //取消订单
    public Rep<Integer> cancelOrder(Long orderId);
}
