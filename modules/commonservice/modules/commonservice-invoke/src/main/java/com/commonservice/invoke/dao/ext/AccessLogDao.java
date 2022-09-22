package com.commonservice.invoke.dao.ext;

import com.commonservice.invoke.model.entity.AccessLog;
import com.commonservice.invoke.model.param.AccessLogQuery;

import java.util.List;

public interface AccessLogDao {

    public int countPage(AccessLogQuery query);

    public List<AccessLog> listPage(AccessLogQuery query);
}
