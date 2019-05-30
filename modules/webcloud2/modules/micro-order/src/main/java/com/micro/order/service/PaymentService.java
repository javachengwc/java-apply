package com.micro.order.service;

import com.micro.order.model.pojo.ShopOrder;

public interface PaymentService {

    public boolean pay(ShopOrder order);
}
