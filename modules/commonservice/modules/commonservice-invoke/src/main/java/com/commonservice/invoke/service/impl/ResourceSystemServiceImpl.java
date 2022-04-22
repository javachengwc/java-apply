package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.ResourceSystemMapper;
import com.commonservice.invoke.model.entity.ResourceSystem;
import com.commonservice.invoke.service.ResourceSystemService;
import org.springframework.stereotype.Service;

@Service
public class ResourceSystemServiceImpl extends ServiceImpl<ResourceSystemMapper, ResourceSystem>
        implements ResourceSystemService {

}