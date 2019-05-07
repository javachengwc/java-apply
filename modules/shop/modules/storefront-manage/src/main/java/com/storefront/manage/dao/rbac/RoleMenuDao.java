package com.storefront.manage.dao.rbac;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMenuDao {

    //新增角色菜单
    public int addRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") Long[] menuIds);

    public int deleteByRole(@Param("roleId") Long roleId);

    //获取角色下的菜单ID
    public List<Long> queryMenuIdsByRole(@Param("roleId") Long roleId);
}
