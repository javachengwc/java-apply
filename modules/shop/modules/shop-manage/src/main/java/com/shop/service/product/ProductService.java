package com.shop.service.product;

import com.shop.dao.mapper.PdProductMapper;
import com.shop.model.pojo.PdProduct;
import com.shop.model.pojo.PdProductExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务类
 */
@Service
public class ProductService {

    @Autowired
    private PdProductMapper pdProductMapper;

    public int count(String productId,String name,Integer sellerId)
    {
        PdProductExample example = new PdProductExample();
        PdProductExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isBlank(productId))
        {
            criteria.andProductIdEqualTo(productId);
        }
        if(!StringUtils.isBlank(name))
        {
            criteria.andNameEqualTo(name);
        }
        if(sellerId!=null)
        {
            criteria.andSellerIdEqualTo(sellerId);
        }
        return pdProductMapper.countByExample(example);
    }

    public List<PdProduct> queryPage(String productId,String name,Integer sellerId,int start ,int rows)
    {
        PdProductExample example = new PdProductExample();
        PdProductExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isBlank(productId))
        {
            criteria.andProductIdEqualTo(productId);
        }
        if(!StringUtils.isBlank(name))
        {
            criteria.andNameEqualTo(name);
        }
        if(sellerId!=null)
        {
            criteria.andSellerIdEqualTo(sellerId);
        }
        example.setOrderByClause(" id desc limit "+start +","+rows);

        return pdProductMapper.selectByExample(example);
    }
}
