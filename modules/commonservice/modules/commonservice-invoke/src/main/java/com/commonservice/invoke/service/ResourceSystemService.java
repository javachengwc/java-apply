package com.commonservice.invoke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.commonservice.invoke.model.entity.ResourceSystem;

import java.util.List;

public interface ResourceSystemService extends IService<ResourceSystem>  {

    public List<ResourceSystem> listBySort();
}
