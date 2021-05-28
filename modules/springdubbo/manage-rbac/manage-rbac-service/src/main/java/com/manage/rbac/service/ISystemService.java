package com.manage.rbac.service;

import com.manage.rbac.entity.System;

import java.util.List;

/**
 * System的服务接口
 */
public interface ISystemService {

    //查询所有可展示的的系统
    public List<System> listAbleSystem();
}
