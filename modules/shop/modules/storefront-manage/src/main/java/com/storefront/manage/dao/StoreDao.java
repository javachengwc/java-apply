package com.storefront.manage.dao;

import com.storefront.manage.model.vo.StoreQueryVo;
import com.storefront.manage.model.vo.StoreVo;

import java.util.List;

public interface StoreDao {

    public List<StoreVo> queryPage(StoreQueryVo queryVo);
}
