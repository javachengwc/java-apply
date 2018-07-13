package com.sharding.springdemo.service;

import com.sharding.springdemo.model.vo.OrderItemVo;
import com.sharding.springdemo.model.vo.OrderQueryVo;
import com.sharding.springdemo.model.vo.OrderVo;
import com.util.page.Page;

import java.util.List;

public interface OrderService {

    public OrderVo createOrder(OrderVo orderVo);

    public Page<OrderVo> queryPage(OrderQueryVo queryVo);

    public List<OrderItemVo> queryOrderItem(Long orderId);
}
