package com.storefront.manage.service;

import com.shop.base.model.Page;
import com.storefront.manage.model.vo.StoreQueryVo;
import com.storefront.manage.model.vo.StoreVo;

public interface StoreService {

    public Page<StoreVo> queryPage(StoreQueryVo queryVo);
}
