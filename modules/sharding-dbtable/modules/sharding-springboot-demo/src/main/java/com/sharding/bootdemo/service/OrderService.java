package com.sharding.bootdemo.service;

import com.sharding.bootdemo.model.vo.OrderItemVo;
import com.sharding.bootdemo.model.vo.OrderQueryVo;
import com.sharding.bootdemo.model.vo.OrderVo;
import com.util.page.Page;

import java.util.List;

public interface OrderService {

    public OrderVo createOrder(OrderVo orderVo);

    public Page<OrderVo> queryPage(OrderQueryVo queryVo);

    public List<OrderItemVo> queryOrderItem(Long orderId);
}
