package com.manage.rbac.dao;

import com.manage.rbac.entity.RoleMenu;
import com.manage.rbac.entity.RoleMenuExample;
import org.springframework.stereotype.Repository;

/**
 * RoleMenuMapper继承基类
 */
@Repository
public interface RoleMenuMapper extends MyBatisBaseDao<RoleMenu, Integer, RoleMenuExample> {
}