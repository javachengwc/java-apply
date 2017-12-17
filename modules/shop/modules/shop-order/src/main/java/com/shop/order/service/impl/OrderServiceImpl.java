package com.shop.order.service.impl;

import com.shop.order.dao.mapper.ShopOrderMapper;
import com.shop.order.model.OrderInfo;
import com.shop.order.model.pojo.ShopOrder;
import com.shop.order.service.OrderService;
import com.util.date.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ShopOrderMapper shopOrderMapper;

    public ShopOrder getById(Long orderId) {

        return shopOrderMapper.selectByPrimaryKey(orderId);
    }

    public OrderInfo getOrderInfo(Long orderId) {

        ShopOrder shopOrder = getById(orderId);
        if(shopOrder==null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(shopOrder,orderInfo);
        //删除时间
        Date cancelTime = orderInfo.getCancelTime();
        String cancelTimeStr=transDateTimeStr(cancelTime);
        orderInfo.setCancelTimeStr(cancelTimeStr);

        //支付时间
        Date payTime = orderInfo.getPayTime();
        String payTimeStr=transDateTimeStr(payTime);
        orderInfo.setPayTimeStr(payTimeStr);

        //发货时间
        Date deliverTime = orderInfo.getDeliverTime();
        String deliverTimeStr=transDateTimeStr(deliverTime);
        orderInfo.setDeliverTimeStr(deliverTimeStr);

        //收货时间
        Date receiveTime = orderInfo.getReceiveTime();
        String receiveTimeStr=transDateTimeStr(receiveTime);
        orderInfo.setReceiveTimeStr(receiveTimeStr);

        //创建时间
        Date creatTime = orderInfo.getCreateTime();
        String creatTimeStr=transDateTimeStr(creatTime);
        orderInfo.setCreateTimeStr(creatTimeStr);

        return orderInfo;
    }

    public String transDateTimeStr(Date date) {
        if(date==null) {
            return "";
        }
        return DateUtil.formatDate(date,DateUtil.FMT_YMD_HMS);
    }
}
