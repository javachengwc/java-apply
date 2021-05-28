package com.manage.rbac.dao;

import com.manage.rbac.entity.OperateLog;
import com.manage.rbac.entity.OperateLogExample;
import org.springframework.stereotype.Repository;

/**
 * OperateLogMapper继承基类
 */
@Repository
public interface OperateLogMapper extends MyBatisBaseDao<OperateLog, Long, OperateLogExample> {
}