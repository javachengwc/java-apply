package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.AccessResourceMapper;
import com.commonservice.invoke.dao.ext.AccessResourceDao;
import com.commonservice.invoke.model.entity.AccessResource;
import com.commonservice.invoke.model.param.ResourceQuery;
import com.commonservice.invoke.service.AccessResourceService;
import com.util.JsonUtil;
import com.util.page.Page;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccessResourceServiceImpl extends ServiceImpl<AccessResourceMapper, AccessResource>
    implements AccessResourceService {

    @Autowired
    private AccessResourceDao accessResourceDao;

    public int countPage(ResourceQuery resourceQuery) {
        return accessResourceDao.countPage(resourceQuery);
    }

    public List<AccessResource> listPage(ResourceQuery resourceQuery) {
        return accessResourceDao.listPage(resourceQuery);
    }

    public Page<AccessResource> page(ResourceQuery resourceQuery) {
        log.info("AccessResourceServiceImpl page start ,resourceQuery={}", JsonUtil.obj2Json(resourceQuery));
        return null;
    }
}
