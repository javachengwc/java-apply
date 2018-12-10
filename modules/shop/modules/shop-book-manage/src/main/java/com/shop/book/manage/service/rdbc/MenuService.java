package com.shop.book.manage.service.rdbc;

import com.shop.book.manage.model.pojo.Menu;
import com.shop.book.manage.model.vo.MenuVo;

import java.util.List;

public interface MenuService {

    public Menu getById(Long id);

    public List<Menu>  queryByName(String name);

    public Menu addMenu(Menu menu);

    public Integer uptMenu(Menu menu);

    public Integer delMenus(List<Long> ids);

    //父id是parentId的菜单记录数
    public Integer countByParent(Long parentId);

    //菜单树
    public List<MenuVo> queryMenuTree();
}
