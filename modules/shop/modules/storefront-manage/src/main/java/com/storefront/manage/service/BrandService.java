package com.storefront.manage.service;

import com.shop.base.model.Page;
import com.storefront.manage.model.vo.BrandQueryVo;
import com.storefront.manage.model.vo.BrandVo;

public interface BrandService {

    public Page<BrandVo> queryPage(BrandQueryVo queryVo);
}
