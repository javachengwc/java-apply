package com.manage.rbac.provider.impl;

import com.model.base.Resp;
import com.manage.rbac.model.common.Constant;
import com.manage.rbac.provider.ISystemProvider;
import com.manage.rbac.service.ISystemService;
import com.manage.rbac.model.dto.SystemDTO;
import com.manage.rbac.entity.System;

import com.util.TransUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = Constant.DUBBO_API_VERSION)
public class SystemProviderImpl implements ISystemProvider {

    @Autowired
    private ISystemService service;

    //查询所有可看的的系统
    @Override
    public Resp<List<SystemDTO>> listAbleSystem() {
        List<System> list =service.listAbleSystem();
        List<SystemDTO> rtList = TransUtil.transList(list,SystemDTO.class);
        return Resp.data(rtList);
    }
}