package com.sharding.junior.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharding.junior.dao.mapper.OrderMapper;
import com.sharding.junior.model.entity.Order;
import com.sharding.junior.model.vo.OrderVo;
import com.sharding.junior.service.OrderService;
import com.tool.util.TransUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static Logger logger= LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order queryByOrderNo(String orderNo) {
        Order order =orderMapper.selectOne(new QueryWrapper<Order>().eq("order_no",orderNo));
        return order;
    }

    public Order createOrder(OrderVo orderVo) {
        logger.info("OrderServiceImpl createOrder start,orderVo={}",orderVo);
        Date now = new Date();
        Order order= TransUtil.transEntity(orderVo,Order.class);
        String orderNo = UUID.randomUUID().toString();
        order.setOrderNo(orderNo);
        order.setStatu(1);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        orderMapper.insert(order);
        Long orderId = order.getId();
        logger.info("OrderServiceImpl createOrder end,id={},orderNo={}",orderId,orderNo);
        order = orderMapper.selectById(orderId);
        return order;
    }

}
