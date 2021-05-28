package com.manage.rbac.dao.ext;

import com.manage.rbac.entity.Menu;
import com.manage.rbac.entity.ext.MenuDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDao {

    //查询菜单详情
    public MenuDO getMenu(@Param("id") Integer id);

    //根据菜单删除角色菜单
    public Integer deleteRoleMenuByMenu(@Param("menuId") Integer menuId);

    //根据角色删除角色菜单
    public Integer deleteRoleMenuByRole(@Param("roleId") Integer roleId);

    //查询角色关联的菜单
    public List<Menu> listMenuByRole(@Param("roleId") Integer roleId);

    //根据用户岗位查询用户菜单
    public List<Menu> listMenuByUserPost(@Param("userId") Integer userId);

    //根据用户用户组查询用户菜单
    public List<Menu> listMenuByUserCrowd(@Param("userId") Integer userId);

    //查询所有可用的菜单
    public List<Menu> listAbleMenu();

    //添加角色菜单
    public Integer addRoleMenu(@Param("roleId") Integer roleId,@Param("menuIdList") List<Integer> menuIdList);
}
