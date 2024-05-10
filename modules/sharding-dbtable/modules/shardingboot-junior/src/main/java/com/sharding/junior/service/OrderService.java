package com.sharding.junior.service;


import com.sharding.junior.model.entity.Order;
import com.sharding.junior.model.vo.OrderVo;

public interface OrderService {

    Order queryByOrderNo(String orderNo);

    Order createOrder(OrderVo orderVo);
}
