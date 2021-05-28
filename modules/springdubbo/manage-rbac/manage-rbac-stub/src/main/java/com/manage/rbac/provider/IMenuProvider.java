package com.manage.rbac.provider;

import com.manage.rbac.model.dto.MenuDTO;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.SystemMenuDTO;
import com.model.base.Resp;

import java.util.List;

public interface IMenuProvider {

    //查询菜单详情
    public Resp<MenuDTO> getMenuDetail(Integer id);

    //根据父级和名称查询数量
    public Resp<Long> countByParentAndName(Integer parentId,String name);

    //查询子菜单数量
    public Resp<Long> countChild(Integer id);

    //增加菜单
    public Resp<Void> addMenu(MenuDTO menuDTO);

    //修改菜单
    public Resp<Void> updateMenu(MenuDTO menuDTO);

    //启用菜单
    public Resp<Void> enableMenu(Integer id,OperatorDTO operator);

    //禁用菜单
    public Resp<Void> disableMenu(Integer id,OperatorDTO operator);

    //删除菜单
    public Resp<Void> deleteMenu(Integer id);

    //查询系统菜单树
    public Resp<List<MenuDTO>> listMenuTreeBySystem(Integer systemId);

    //查询角色关联系统菜单
    public Resp<List<SystemMenuDTO>> listSystemMenuByRole(Integer roleId);

    //查询用户菜单
    public Resp<List<SystemMenuDTO>> listSystemMenuByUser(Integer userId);

    //修改角色系统菜单
    public Resp<Void> updateRoleMenu(Integer roleId,List<SystemMenuDTO> systemMenuList);

    public Resp<MenuDTO> getById(Integer id);
}