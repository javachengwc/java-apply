package com.sharding.bootdemo.dao;

import com.sharding.bootdemo.model.Order;
import com.sharding.bootdemo.model.OrderItem;
import com.sharding.bootdemo.model.vo.OrderQueryVo;

import java.util.List;

public interface OrderDao {

    public Integer insertOrder(Order order);

    public Integer insertOrderItem(OrderItem orderItem);

    public Integer count(OrderQueryVo queryVo);

    public List<Order> queryList(OrderQueryVo queryVo);

    public List<OrderItem> queryOrderItem(Long orderId);
}
