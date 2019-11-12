package com.micro.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.order.dao.mapper.ShopOrderMapper;
import com.micro.order.dao.plus.ShopOrderPlusMapper;
import com.micro.order.enums.OrderStatuEnum;
import com.micro.order.model.pojo.ShopOrder;
import com.micro.order.model.pojo.ShopOrderExample;
import com.micro.order.model.req.OrderReq;
import com.micro.order.service.OrderService;
import com.micro.order.service.PaymentService;
import com.util.TransUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderServiceImpl extends ServiceImpl<ShopOrderPlusMapper, ShopOrder> implements OrderService {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Autowired
    private PaymentService paymentService;

    //下单
    public ShopOrder order(OrderReq orderReq) {
        logger.info("OrderServiceImpl order start, orderReq={}",orderReq);
        Date now = new Date();
        ShopOrder order = TransUtil.transEntity(orderReq,ShopOrder.class);
        order.setCreateTime(now);
        order.setModifiedTime(now);
        order.setStatu(OrderStatuEnum.INIT.getValue());
        shopOrderMapper.insert(order);
        Long orderId = order.getId();
        logger.info("OrderServiceImpl order 保存订单记录完成，orderId={}",orderId);
        paymentService.pay(order);
        return order;
    }

    //修改订单状态
    public void changeStatu(Long orderId,Integer curStatu,Integer newStatu) {
        ShopOrderExample example = new ShopOrderExample();
        ShopOrderExample.Criteria criteria =example.createCriteria();
        criteria.andIdEqualTo(orderId);
        criteria.andStatuEqualTo(curStatu);

        ShopOrder order = new ShopOrder();
        order.setStatu(newStatu);
        order.setModifiedTime(new Date());

        shopOrderMapper.updateByExampleSelective(order,example);
    }
}
