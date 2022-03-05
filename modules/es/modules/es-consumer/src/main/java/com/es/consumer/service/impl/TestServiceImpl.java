package com.es.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.es.consumer.dao.mapper.TestMapper;
import com.es.consumer.model.entity.Test;
import com.es.consumer.service.TestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

    @Override
    public List<Test> queryByName(String name) {
        Test user = new Test();
        user.setName(name);
        Wrapper<Test> wrapper = new QueryWrapper<Test>(user);
        List<Test> list = this.baseMapper.selectList(wrapper);
        return list;
    }
}