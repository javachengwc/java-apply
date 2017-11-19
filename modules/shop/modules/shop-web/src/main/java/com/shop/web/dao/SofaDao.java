package com.shop.web.dao;

import com.shop.web.model.pojo.SfProduct;
import com.shop.web.model.vo.SofaQueryVo;

import java.util.List;

public interface SofaDao {

    public List<SfProduct> queryPage(SofaQueryVo queryVo);

    public Integer count(SofaQueryVo queryVo);
}
