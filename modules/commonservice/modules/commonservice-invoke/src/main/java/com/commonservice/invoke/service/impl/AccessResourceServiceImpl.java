package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.AccessResourceMapper;
import com.commonservice.invoke.dao.ext.AccessResourceDao;
import com.commonservice.invoke.model.entity.AccessResource;
import com.commonservice.invoke.model.param.AccessResourceQuery;
import com.commonservice.invoke.model.vo.AccessResourceVo;
import com.commonservice.invoke.service.AccessResourceService;
import com.model.base.PageVo;
import com.util.JsonUtil;
import com.util.TransUtil;
import com.util.base.StringUtil;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccessResourceServiceImpl extends ServiceImpl<AccessResourceMapper, AccessResource>
    implements AccessResourceService {

    @Autowired
    private AccessResourceDao accessResourceDao;

    public int countPage(AccessResourceQuery resourceQuery) {
        return accessResourceDao.countPage(resourceQuery);
    }

    public List<AccessResource> listPage(AccessResourceQuery resourceQuery) {
        return accessResourceDao.listPage(resourceQuery);
    }

    public PageVo<AccessResourceVo> page(AccessResourceQuery query) {
        log.info("AccessResourceServiceImpl page start ,resourceQuery={}", JsonUtil.obj2Json(query));
        int pageNum = query.getPageNo();
        int pageSize = query.getPageSize();

        int total = accessResourceDao.countPage(query);

        PageVo<AccessResourceVo> page = new PageVo<AccessResourceVo>();
        page.setTotalCount(total);

        if(page.isBound(pageNum,pageSize)) {
            page.setList(Collections.EMPTY_LIST);
            return page;
        }

        if(StringUtils.isNotBlank(query.getOrderBy())) {
            query.setOrderBy(StringUtil.field2Col(query.getOrderBy()));
        }
        List<AccessResource> list = accessResourceDao.listPage(query);
        List<AccessResourceVo> voList = TransUtil.transListWithJson(list, AccessResourceVo.class);
        page.setList(voList);
        return page;
    }

    public List<AccessResource> queryAnalyResourceBySys(Long sysId) {
        AccessResource cdn = new AccessResource();
        cdn.setSysId(sysId);
        cdn.setAnalysisFlag(1);
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.setEntity(cdn);
        List<AccessResource> list = this.list(queryWrapper);
        return list;
    }
}
