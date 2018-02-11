package com.shop.order.service;

import com.shop.order.api.model.OrderInfo;
import com.shop.order.api.model.OrderVo;
import com.shop.order.model.pojo.OrderOperateRecord;
import com.shop.order.model.pojo.OrderStatuChange;
import com.shop.order.model.pojo.ShopOrder;

public interface OrderService {

    public OrderInfo createOrder(OrderVo orderVo);

    public Boolean recordOrderCreate(ShopOrder shopOrder);

    public Integer addOrderOperate(OrderOperateRecord orderOperateRecord);

    public Integer addOrderStatuChange(OrderStatuChange orderStatuChange);

    public OrderStatuChange recordOrderStatuChange(OrderOperateRecord operateRecord);

    public Integer uptOrder(ShopOrder uptOrder);

    public ShopOrder getById(Long orderId);

    public OrderInfo getOrderInfo(Long orderId);

    public OrderInfo getOrderInfo2(Long orderId);

}
