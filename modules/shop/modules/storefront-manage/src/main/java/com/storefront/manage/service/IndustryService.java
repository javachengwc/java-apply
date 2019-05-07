package com.storefront.manage.service;

import com.shop.base.model.Page;
import com.storefront.manage.model.vo.IndustryQueryVo;
import com.storefront.manage.model.vo.IndustryVo;

public interface IndustryService {

    public Page<IndustryVo> queryPage(IndustryQueryVo queryVo);
}
