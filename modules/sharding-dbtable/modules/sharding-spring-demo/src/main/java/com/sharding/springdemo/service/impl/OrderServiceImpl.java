package com.sharding.springdemo.service.impl;

import com.sharding.springdemo.dao.OrderDao;
import com.sharding.springdemo.model.Order;
import com.sharding.springdemo.model.OrderItem;
import com.sharding.springdemo.model.vo.OrderItemVo;
import com.sharding.springdemo.model.vo.OrderQueryVo;
import com.sharding.springdemo.model.vo.OrderVo;
import com.sharding.springdemo.service.OrderService;
import com.shop.base.util.TransUtil;
import com.util.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static Logger logger= LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    public OrderVo createOrder(OrderVo orderVo) {
        logger.info("OrderServiceImpl createOrder start,orderVo={}",orderVo);
        Date now = new Date();
        Order order= TransUtil.transEntity(orderVo,Order.class);
        order.setCreateTime(now);
        order.setModifiedTime(now);
        orderDao.insertOrder(order);
        Long orderId = order.getOrderId();
        logger.info("OrderServiceImpl createOrder do insertOrder,orderId={}",orderId);
        orderVo.setOrderId(orderId);
        List<OrderItemVo> orderItemList = orderVo.getOrderItemList();
        if(orderItemList!=null) {
            for(OrderItemVo orderItemVo:orderItemList) {
                OrderItem orderItem= TransUtil.transEntity(orderItemVo,OrderItem.class);
                orderItem.setCreateTime(now);
                orderItem.setModifiedTime(now);
                orderItem.setOrderId(orderId);
                orderDao.insertOrderItem(orderItem);
                Long orderItemId= orderItem.getOrderItemId();
                logger.info("OrderServiceImpl createOrder do insertOrderItem,orderItemId={},orderId={}",orderItemId,orderId);
            }
        }
        logger.info("OrderServiceImpl createOrder end,orderId={}",orderId);
        return orderVo;
    }

    public Page<OrderVo> queryPage(OrderQueryVo queryVo) {
        List<OrderVo> rtList=null;
        int count = orderDao.count(queryVo);
        if(count<=0) {
            rtList= Collections.EMPTY_LIST;
        }else {
            List<Order> list = orderDao.queryList(queryVo);
            rtList=TransUtil.transList(list,OrderVo.class);
        }
        Page<OrderVo> page = new Page<OrderVo>();
        page.setTotalCount(count);
        page.setResult(rtList);
        return page;
    }

    public List<OrderItemVo> queryOrderItem(Long orderId) {
        List<OrderItem> list = orderDao.queryOrderItem(orderId);
        List<OrderItemVo> rtList = TransUtil.transList(list,OrderItemVo.class);
        return rtList;
    }
}
