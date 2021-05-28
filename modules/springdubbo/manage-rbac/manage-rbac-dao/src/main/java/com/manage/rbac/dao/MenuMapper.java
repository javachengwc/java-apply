package com.manage.rbac.dao;

import com.manage.rbac.entity.Menu;
import com.manage.rbac.entity.MenuExample;
import org.springframework.stereotype.Repository;

/**
 * MenuMapper继承基类
 */
@Repository
public interface MenuMapper extends MyBatisBaseDao<Menu, Integer, MenuExample> {
}