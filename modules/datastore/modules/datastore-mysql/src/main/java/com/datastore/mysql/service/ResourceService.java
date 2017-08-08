package com.datastore.mysql.service;

import com.datastore.mysql.dao.mapper.ResourceMapper;
import com.datastore.mysql.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    public Resource getByName(String name) {

        return resourceMapper.getByName(name);
    }

    public Resource getById(Integer id) {
        return resourceMapper.getById(id);
    }

    public Integer countAll() {
        return resourceMapper.countAll();
    }

    public List<Resource> queryAll() {
        return resourceMapper.queryAll();
    }
}
