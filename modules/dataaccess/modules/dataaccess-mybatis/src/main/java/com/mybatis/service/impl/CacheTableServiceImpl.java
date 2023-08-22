package com.mybatis.service.impl;

import com.mybatis.dao.mapper.CacheTableMapper;
import com.mybatis.model.entity.CacheTable;
import com.mybatis.model.entity.CacheTableExample;
import com.mybatis.service.CacheTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CacheTableServiceImpl implements CacheTableService {

    @Autowired
    private CacheTableMapper cacheTableMapper;

    @Transactional(rollbackFor=Exception.class)
    @Override
    public List<CacheTable> queryList() {
        CacheTableExample example = new CacheTableExample();
        List<CacheTable> list=cacheTableMapper.selectByExample(example);
        return list;
    }
}
