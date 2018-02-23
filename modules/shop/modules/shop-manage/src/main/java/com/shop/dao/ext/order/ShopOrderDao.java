package com.shop.dao.ext.order;

import com.shop.model.pojo.ShopOrder;
import com.shop.model.vo.OrderQueryVo;

import java.util.List;

public interface ShopOrderDao {

    public List<ShopOrder> queryOrderPage(OrderQueryVo queryVo);

    public int orderCount(OrderQueryVo queryVo);
}
