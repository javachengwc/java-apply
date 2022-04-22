package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.ResourceParamMapper;
import com.commonservice.invoke.model.entity.ResourceParam;
import com.commonservice.invoke.service.ResourceParamService;
import org.springframework.stereotype.Service;

@Service
public class ResourceParamServiceImpl extends ServiceImpl<ResourceParamMapper, ResourceParam>
        implements ResourceParamService {

}