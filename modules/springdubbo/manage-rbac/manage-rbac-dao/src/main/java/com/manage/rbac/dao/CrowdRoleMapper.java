package com.manage.rbac.dao;

import com.manage.rbac.entity.CrowdRole;
import com.manage.rbac.entity.CrowdRoleExample;
import org.springframework.stereotype.Repository;

/**
 * CrowdRoleMapper继承基类
 */
@Repository
public interface CrowdRoleMapper extends MyBatisBaseDao<CrowdRole, Integer, CrowdRoleExample> {
}