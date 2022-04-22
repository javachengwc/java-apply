package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.ResourceCategoryMapper;
import com.commonservice.invoke.model.entity.ResourceCategory;
import com.commonservice.invoke.service.ResourceCategoryService;
import org.springframework.stereotype.Service;

@Service
public class ResourceCategoryServiceImpl extends ServiceImpl<ResourceCategoryMapper, ResourceCategory>
        implements ResourceCategoryService {

}