package com.datastore.mysql.service;

import com.datastore.mysql.model.entity.Resource;

import java.util.List;

public interface ResourceService {

    public Resource getByName(String name);

    public Resource getById(Integer id);

    public Integer countAll();

    public List<Resource> queryAll();

    public boolean uptByName(Resource paramResource);

    public boolean uptShowByName(String name);

}
