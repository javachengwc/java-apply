package com.shop.service.order;

import com.shop.dao.ext.order.OrderDao;
import com.shop.model.pojo.OdOrder;
import com.shop.model.vo.OrderQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单服务类
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public int count(OrderQueryVo queryVo)
    {
        return orderDao.count(queryVo);
    }

    public List<OdOrder> queryPage(OrderQueryVo queryVo)
    {
        return orderDao.queryPage(queryVo);
    }
}
