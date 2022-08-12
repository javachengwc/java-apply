package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.ResourceHeaderMapper;
import com.commonservice.invoke.model.entity.ResourceHeader;
import com.commonservice.invoke.service.ResourceHeaderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceHeaderServiceImpl extends ServiceImpl<ResourceHeaderMapper, ResourceHeader>
        implements ResourceHeaderService {

    public List<ResourceHeader> queryByResource(Long resourceId) {
        ResourceHeader cdn = new ResourceHeader();
        cdn.setResourceId(resourceId);
        Wrapper<ResourceHeader> wrapper  = new QueryWrapper<ResourceHeader>(cdn);
        List<ResourceHeader> list =this.list(wrapper);
        return list;
    }
}
