package com.manage.rbac.dao;

import com.manage.rbac.entity.System;
import com.manage.rbac.entity.SystemExample;
import org.springframework.stereotype.Repository;

/**
 * SystemMapper继承基类
 */
@Repository
public interface SystemMapper extends MyBatisBaseDao<System, Integer, SystemExample> {
}