package com.manage.rbac.dao;

import com.manage.rbac.entity.Role;
import com.manage.rbac.entity.RoleExample;
import org.springframework.stereotype.Repository;

/**
 * RoleMapper继承基类
 */
@Repository
public interface RoleMapper extends MyBatisBaseDao<Role, Integer, RoleExample> {
}