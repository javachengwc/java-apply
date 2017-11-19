package com.shop.web.service.impl;

import com.shop.web.dao.mapper.SfBrandMapper;
import com.shop.web.model.pojo.SfBrand;
import com.shop.web.model.pojo.SfBrandExample;
import com.shop.web.service.SfBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SfBrandServiceImpl implements SfBrandService{

    @Autowired
    private SfBrandMapper sfBrandMapper;

    public List<SfBrand> queryAbleList() {

        SfBrandExample example = new SfBrandExample();
        SfBrandExample.Criteria  criteria =example.createCriteria();
        criteria.andIsShowEqualTo(1);
        return sfBrandMapper.selectByExample(example);
    }
}
