package com.micro.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.order.dao.mapper.ShopOrderMapper;
import com.micro.order.model.pojo.ShopOrder;
import com.micro.order.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrder> implements OrderService {
}
