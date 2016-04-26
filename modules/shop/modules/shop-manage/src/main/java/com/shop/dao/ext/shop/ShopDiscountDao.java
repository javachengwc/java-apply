package com.shop.dao.ext.shop;

import com.shop.dao.BaseDao;
import com.shop.model.pojo.ShopDiscount;
import com.shop.model.vo.ShopDiscountQueryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商店折扣访问类
 */
@Repository
public class ShopDiscountDao extends BaseDao {

    public int count(ShopDiscountQueryVo queryVo)
    {
        return (Integer)getSqlSession().selectOne("com.shop.dao.ext.shop.ShopDiscountDao.count", queryVo);
    }

    public List<ShopDiscount> queryPage(ShopDiscountQueryVo queryVo)
    {
        return getSqlSession().selectList("com.shop.dao.ext.shop.ShopDiscountDao.queryPage", queryVo);
    }
}