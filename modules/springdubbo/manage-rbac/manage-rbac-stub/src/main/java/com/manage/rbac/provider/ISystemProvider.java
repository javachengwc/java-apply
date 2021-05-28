package com.manage.rbac.provider;

import com.manage.rbac.model.dto.SystemDTO;
import com.model.base.Resp;

import java.util.List;

public interface ISystemProvider {

    //查询所有可用的系统
    public Resp<List<SystemDTO>> listAbleSystem();
}