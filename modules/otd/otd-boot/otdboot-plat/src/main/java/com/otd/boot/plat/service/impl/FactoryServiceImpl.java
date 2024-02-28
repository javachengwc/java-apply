package com.otd.boot.plat.service.impl;

import com.otd.boot.plat.dao.mapper.FactoryMapper;
import com.otd.boot.plat.model.entity.Factory;
import com.otd.boot.plat.model.entity.FactoryExample;
import com.otd.boot.plat.service.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired
    private FactoryMapper factoryMapper;

    @Override
    public Factory queryByFactoryNo(String factoryNo) {
        FactoryExample example = new FactoryExample();
        example.createCriteria().andFactoryNoEqualTo(factoryNo);
        List<Factory> list = factoryMapper.selectByExample(example);
        if(list!=null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
