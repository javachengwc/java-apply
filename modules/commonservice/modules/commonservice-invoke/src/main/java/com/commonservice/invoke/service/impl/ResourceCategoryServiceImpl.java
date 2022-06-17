package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.ResourceCategoryMapper;
import com.commonservice.invoke.model.entity.ResourceCategory;
import com.commonservice.invoke.model.param.ResourceCategoryQuery;
import com.commonservice.invoke.service.ResourceCategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceCategoryServiceImpl extends ServiceImpl<ResourceCategoryMapper, ResourceCategory>
        implements ResourceCategoryService {

    public List<ResourceCategory> queryList(ResourceCategoryQuery query) {
        QueryWrapper<ResourceCategory> queryWrapper = new QueryWrapper<ResourceCategory>();
        ResourceCategory category = new ResourceCategory();
        category.setParentId(query.getParentId());
        category.setSysId(query.getSysId());
        if(StringUtils.isNotBlank(query.getName())) {
            category.setName(query.getName());
        }
        queryWrapper.setEntity(category);
        queryWrapper.orderByAsc("sort");
        List<ResourceCategory> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public List<ResourceCategory> genTree(List<ResourceCategory> list) {
        if(list==null || list.size()<=0) {
            return null;
        }
        List<ResourceCategory> rtList = new ArrayList<ResourceCategory>();
        for (ResourceCategory category : list) {
            if (category.getParentId() == null || category.getParentId() <= 0) {
                rtList.add(this.appendChildren(category, list));
            }
        }
        return rtList;
    }

    private ResourceCategory appendChildren(ResourceCategory parentCategory, List<ResourceCategory> list) {
        for (ResourceCategory per : list) {
            if (per.getParentId() != null && per.getParentId().longValue() == parentCategory.getId()) {
                parentCategory.getChildren().add(this.appendChildren(per, list));
            }
        }
        return parentCategory;
    }
}