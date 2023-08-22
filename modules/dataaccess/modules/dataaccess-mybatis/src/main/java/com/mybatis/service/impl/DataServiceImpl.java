package com.mybatis.service.impl;

import com.mybatis.dao.mapper.DataMapper;
import com.mybatis.model.entity.Data;
import com.mybatis.model.entity.DataExample;
import com.mybatis.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private DataMapper dataMapper;

    @Override
    public List<Data> queryList() {
        DataExample example = new DataExample();
        List<Data> list=dataMapper.selectByExample(example);
        return list;
    }

}
