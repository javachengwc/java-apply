package com.commonservice.invoke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.commonservice.invoke.model.entity.ResourceCategory;

import java.util.List;

public interface ResourceCategoryService extends IService<ResourceCategory> {

    List<ResourceCategory> queryList(String name,Long parentId);

    List<ResourceCategory> genTree(List<ResourceCategory> list);

}
