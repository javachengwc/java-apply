package com.app.service;

import com.app.entity.pojo.SysResource;

public interface ResourceService {

    public SysResource getById(Integer id);

    public SysResource queryByName(String name);
}
