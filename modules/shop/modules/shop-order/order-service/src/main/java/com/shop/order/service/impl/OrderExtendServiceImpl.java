package com.shop.order.service.impl;

import com.shop.order.api.model.base.Rep;
import com.shop.order.service.OrderExtendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderExtendServiceImpl implements OrderExtendService {

    private static Logger logger= LoggerFactory.getLogger(OrderExtendServiceImpl.class);

    //取消订单
    public Rep<Integer> cancelOrder(Long orderId) {
        return null;
    }
}
