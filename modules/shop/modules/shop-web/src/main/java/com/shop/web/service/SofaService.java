package com.shop.web.service;

import com.shop.web.model.pojo.SfProduct;
import com.shop.web.model.vo.SofaQueryVo;

import java.util.List;

public interface SofaService {

    public List<SfProduct> queryPage(SofaQueryVo queryVo);

    public Integer count(SofaQueryVo queryVo);
}
