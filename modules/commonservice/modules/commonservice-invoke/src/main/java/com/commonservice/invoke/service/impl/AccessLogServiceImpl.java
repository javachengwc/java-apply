package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.AccessLogMapper;
import com.commonservice.invoke.dao.ext.AccessLogDao;
import com.commonservice.invoke.model.entity.AccessLog;
import com.commonservice.invoke.model.param.AccessLogQuery;
import com.commonservice.invoke.service.AccessLogService;
import com.model.base.PageVo;
import com.util.JsonUtil;
import com.util.base.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class AccessLogServiceImpl extends ServiceImpl<AccessLogMapper, AccessLog> implements AccessLogService {

    @Autowired
    private AccessLogDao accessLogDao;

    @Override
    public PageVo<AccessLog> page(AccessLogQuery query) {
        log.info("AccessLogServiceImpl page start ,query={}", JsonUtil.obj2Json(query));
        int pageNum = query.getPageNum();
        int pageSize = query.getPageSize();
        int total = accessLogDao.countPage(query);

        PageVo<AccessLog> page = new PageVo<AccessLog>();
        page.setTotalCount(total);

        if(page.isBound(pageNum,pageSize)) {
            page.setList(Collections.EMPTY_LIST);
            return page;
        }

        if(StringUtils.isNotBlank(query.getOrderBy())) {
            query.setOrderBy(StringUtil.field2Col(query.getOrderBy()));
        }
        List<AccessLog> list = accessLogDao.listPage(query);
        page.setList(list);
        return page;
    }
}
