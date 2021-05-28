package com.manage.rbac.service.impl;

import com.manage.rbac.dao.SystemMapper;
import com.manage.rbac.entity.SystemExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manage.rbac.entity.System;
import com.manage.rbac.service.ISystemService;

import java.util.List;

/**
 * System的服务接口的实现类
 */
@Slf4j
@Service
public class SystemServiceImpl implements ISystemService {

    @Autowired
    private SystemMapper mapper;

    //查询所有可展示的的系统
    @Override
    public List<System> listAbleSystem() {
        SystemExample example = new SystemExample();
        SystemExample.Criteria criteria = example.createCriteria();
        //可用，未删除
        criteria.andDisableEqualTo(Boolean.FALSE);
        example.setOrderByClause(" sort asc");
        List<System> list = mapper.selectByExample(example);
        return list;
    }
}