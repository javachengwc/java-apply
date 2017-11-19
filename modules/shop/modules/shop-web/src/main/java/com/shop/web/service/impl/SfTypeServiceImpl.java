package com.shop.web.service.impl;

import com.shop.web.dao.mapper.SfTypeMapper;
import com.shop.web.model.pojo.SfType;
import com.shop.web.model.pojo.SfTypeExample;
import com.shop.web.service.SfTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SfTypeServiceImpl implements SfTypeService {

    @Autowired
    private SfTypeMapper sfTypeMapper;

    public List<SfType> queryAll() {
        SfTypeExample example = new SfTypeExample();
        return sfTypeMapper.selectByExample(example);
    }
}