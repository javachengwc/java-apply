package com.storefront.manage.dao;

import com.storefront.manage.model.vo.BrandQueryVo;
import com.storefront.manage.model.vo.BrandVo;

import java.util.List;

public interface BrandDao {

    public List<BrandVo> queryPage(BrandQueryVo queryVo);
}
