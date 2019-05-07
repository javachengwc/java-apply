package com.storefront.manage.service.rbac;

import com.shop.base.model.Page;
import com.storefront.manage.model.pojo.rbac.Menu;
import com.storefront.manage.model.vo.MenuQueryVo;
import com.storefront.manage.model.vo.MenuVo;

import java.util.List;

public interface MenuService {

    public Menu getById(Long id);

    public List<Menu>  queryByName(String name);

    public Menu addMenu(Menu menu);

    public Integer uptMenu(Menu menu);

    public Integer delMenus(List<Long> ids);

    //父id是parentId的菜单记录数
    public Integer countByParent(Long parentId);

    public Page<MenuVo> queryPage(MenuQueryVo queryVo);

    //菜单树
    public List<MenuVo> queryMenuTree();

    //查询用户可见的菜单
    public List<MenuVo> queryUserMenu(Long userId);

    //查询类型是菜单的菜单列表
    public List<MenuVo> queryOnlyMenuList();

    //是否还有子菜单
    public boolean hasChildren(Long pMenuId);

    //批量删除菜单
    public Integer batchDel(List<Long> menuIds);
}
