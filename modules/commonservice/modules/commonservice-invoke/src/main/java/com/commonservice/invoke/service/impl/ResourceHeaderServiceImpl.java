package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.ResourceHeaderMapper;
import com.commonservice.invoke.model.entity.ResourceHeader;
import com.commonservice.invoke.service.ResourceHeaderService;
import org.springframework.stereotype.Service;

@Service
public class ResourceHeaderServiceImpl extends ServiceImpl<ResourceHeaderMapper, ResourceHeader>
        implements ResourceHeaderService {

}
