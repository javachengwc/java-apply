package com.shop.book.manage.service.rdbc;

import com.shop.base.model.Page;
import com.shop.book.manage.model.pojo.Menu;
import com.shop.book.manage.model.vo.MenuQueryVo;
import com.shop.book.manage.model.vo.MenuVo;
import com.shop.book.manage.model.vo.RoleQueryVo;
import com.shop.book.manage.model.vo.RoleVo;

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
}