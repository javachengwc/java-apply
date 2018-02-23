package com.shop.service.order;

import com.shop.dao.ext.order.OrderDao;
import com.shop.dao.ext.order.ShopOrderDao;
import com.shop.model.pojo.OdOrder;
import com.shop.model.pojo.ShopOrder;
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

    @Autowired
    private ShopOrderDao shopOrderDao;

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

    public List<ShopOrder> queryOrderPage(OrderQueryVo queryVo)
    {
        return shopOrderDao.queryOrderPage(queryVo);
    }

    public int orderCount(OrderQueryVo queryVo) {
        return shopOrderDao.orderCount(queryVo);
    }
}
