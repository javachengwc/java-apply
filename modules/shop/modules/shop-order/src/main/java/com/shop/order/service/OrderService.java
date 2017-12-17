package com.shop.order.service;

import com.shop.order.model.OrderInfo;
import com.shop.order.model.pojo.ShopOrder;

public interface OrderService {

    public ShopOrder getById(Long orderId);

    public OrderInfo getOrderInfo(Long orderId);
}
