package com.shop.web.service.impl;

import com.shop.web.dao.mapper.SfMaterMapper;
import com.shop.web.model.pojo.SfMater;
import com.shop.web.model.pojo.SfMaterExample;
import com.shop.web.service.SfMaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SfMaterServiceImpl implements SfMaterService {

    @Autowired
    private SfMaterMapper sfMaterMapper;

    public List<SfMater> queryAll() {
        SfMaterExample example = new SfMaterExample();
        return sfMaterMapper.selectByExample(example);
    }
}
