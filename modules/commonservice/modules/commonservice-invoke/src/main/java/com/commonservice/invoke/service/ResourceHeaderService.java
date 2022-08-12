package com.commonservice.invoke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.commonservice.invoke.model.entity.ResourceHeader;

import java.util.List;

public interface ResourceHeaderService extends IService<ResourceHeader> {

    List<ResourceHeader> queryByResource(Long resourceId);
}
