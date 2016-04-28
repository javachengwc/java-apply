package com.shop.service.shop;

import com.shop.dao.ext.shop.ShopDiscountDao;
import com.shop.model.pojo.ShDiscount;
import com.shop.model.vo.ShopDiscountQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商店折扣服务类
 */
@Service
public class ShopDiscountService {

    @Autowired
    private ShopDiscountDao shopDiscountDao;

    public void setShopDiscountDao(ShopDiscountDao shopDiscountDao) {
        this.shopDiscountDao = shopDiscountDao;
    }

    public int count(ShopDiscountQueryVo queryVo)
    {
        return shopDiscountDao.count(queryVo);
    }

    public List<ShDiscount> queryPage(ShopDiscountQueryVo queryVo)
    {
        return shopDiscountDao.queryPage(queryVo);
    }

}
