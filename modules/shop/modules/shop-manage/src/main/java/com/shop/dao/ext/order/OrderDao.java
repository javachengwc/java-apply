package com.shop.dao.ext.order;

import com.shop.dao.BaseDao;
import com.shop.model.pojo.OdOrder;
import com.shop.model.vo.OrderQueryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单访问类
 */
@Repository
public class OrderDao extends BaseDao {

    public int count(OrderQueryVo queryVo)
    {
        return (Integer)getSqlSession().selectOne("com.shop.dao.ext.order.OrderDao.count", queryVo);
    }

    public List<OdOrder> queryPage(OrderQueryVo queryVo)
    {
        return getSqlSession().selectList("com.shop.dao.ext.order.OrderDao.queryPage", queryVo);
    }
}
