package com.commonservice.invoke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.commonservice.invoke.model.entity.AccessLog;
import com.commonservice.invoke.model.param.AccessLogQuery;
import com.model.base.PageVo;

public interface AccessLogService  extends IService<AccessLog> {

    public PageVo<AccessLog> page(AccessLogQuery query);
}
