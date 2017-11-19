package com.shop.web.service.impl;

import com.shop.web.dao.mapper.SfStyleMapper;
import com.shop.web.model.pojo.SfStyle;
import com.shop.web.model.pojo.SfStyleExample;
import com.shop.web.service.SfStyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SfStyleServiceImpl implements SfStyleService {

    @Autowired
    private SfStyleMapper sfStyleMapper;

    public List<SfStyle> queryAll() {
        SfStyleExample example = new SfStyleExample();
        return sfStyleMapper.selectByExample(example);
    }
}