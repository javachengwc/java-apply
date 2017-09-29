package com.app.service.impl;

import com.app.dao.mapper.SysResourceMapper;
import com.app.entity.pojo.SysResource;
import com.app.entity.pojo.SysResourceExample;
import com.app.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService{

    @Autowired
    private SysResourceMapper sysResourceMapper;

    public SysResource getById(Integer id) {
        return sysResourceMapper.selectByPrimaryKey(id);
    }

    public SysResource queryByName(String name) {
        SysResourceExample example = new SysResourceExample();
        SysResourceExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<SysResource> list =sysResourceMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }
}
