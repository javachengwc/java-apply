package com.commonservice.invoke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.commonservice.invoke.model.entity.ResourceCategory;
import com.commonservice.invoke.model.param.ResourceCategoryQuery;

import java.util.List;

public interface ResourceCategoryService extends IService<ResourceCategory> {

    List<ResourceCategory> queryList(ResourceCategoryQuery query);

    List<ResourceCategory> genTree(List<ResourceCategory> list);

}
