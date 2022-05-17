package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.ResourceSystemMapper;
import com.commonservice.invoke.model.entity.ResourceSystem;
import com.commonservice.invoke.service.ResourceSystemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceSystemServiceImpl extends ServiceImpl<ResourceSystemMapper, ResourceSystem>
        implements ResourceSystemService {

    @Override
    public List<ResourceSystem> listBySort() {
        QueryWrapper<ResourceSystem> queryWrapper =new QueryWrapper<ResourceSystem>();
        queryWrapper.orderByAsc("sort");
        List<ResourceSystem> list = this.list(queryWrapper);
        return list;
    }
}