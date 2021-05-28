package com.manage.rbac.service;

import com.manage.rbac.entity.Menu;
import com.manage.rbac.model.dto.MenuDTO;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.SystemMenuDTO;

import java.util.List;

/**
 * Menu的服务接口
 */
public interface IMenuService {

    //查询菜单详情
    public MenuDTO getMenuDetail(Integer id);

    //根据父级和名称查询数量
    public long countByParentAndName(Integer parentId,String name);

    //查询子菜单数量
    public long countChild(Integer id);

    //添加菜单
    public boolean addMenu(MenuDTO menuDTO);

    //修改菜单
    public boolean updateMenu(MenuDTO menuDTO);

    //启用菜单
    public boolean enableMenu(Integer id, OperatorDTO operator);

    //禁用菜单
    public boolean disableMenu(Integer id, OperatorDTO operator);

    //删除菜单
    public boolean deleteMenu(Integer id);

    //查询系统菜单树
    public List<MenuDTO> listMenuTreeBySystem(Integer systemId);

    //生成菜单树
    public List<MenuDTO> genTree(List<Menu> list);

    //查询角色关联系统菜单
    public List<SystemMenuDTO> listSystemMenuByRole(Integer roleId);

    //查询用户菜单
    public List<SystemMenuDTO> listSystemMenuByUser(Integer userId);

    //修改角色系统菜单
    public boolean updateRoleMenu(Integer roleId,List<SystemMenuDTO> systemMenuList);
}
