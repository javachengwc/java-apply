package com.shop.order.service;

import com.shop.order.api.model.base.Rep;
import com.shop.order.model.pojo.ShopOrder;

public interface OrderExtendService {

    //取消订单
    public Rep<Integer> cancelOrder(Long orderId);

    public Boolean innerCancelOrder(ShopOrder shopOrder);
}
