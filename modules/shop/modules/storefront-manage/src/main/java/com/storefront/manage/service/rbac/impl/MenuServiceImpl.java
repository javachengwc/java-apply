package com.storefront.manage.service.rbac.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.base.model.Page;
import com.shop.base.util.TransUtil;
import com.storefront.manage.dao.rbac.MenuDao;
import com.storefront.manage.dao.rbac.mapper.MenuMapper;
import com.storefront.manage.model.pojo.rbac.Menu;
import com.storefront.manage.model.pojo.rbac.MenuExample;
import com.storefront.manage.model.vo.MenuQueryVo;
import com.storefront.manage.model.vo.MenuVo;
import com.storefront.manage.service.rbac.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuDao menuDao;

    public Menu getById(Long id) {
        Menu menu = menuMapper.selectByPrimaryKey(id);
        return menu;
    }

    public List<Menu>  queryByName(String name) {
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<Menu> list = menuMapper.selectByExample(example);
        return list;
    }

    public Menu addMenu(Menu menu) {
        int rt = menuMapper.insertSelective(menu);
        return menu;
    }

    public Integer uptMenu(Menu menu) {
        int rt = menuMapper.updateByPrimaryKeySelective(menu);
        return rt;
    }

    public Integer delMenus(List<Long> ids) {
        if(ids==null || ids.size()<=0) {
            return 0;
        }
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);

        int rt = menuMapper.deleteByExample(example);
        return rt;
    }

    //父id是parentId的菜单记录数
    public Integer countByParent(Long parentId) {
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);

        int count = menuMapper.countByExample(example);
        return count;
    }

    public Page<MenuVo> queryPage(MenuQueryVo queryVo) {
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        List<MenuVo> list = menuDao.queryPage(queryVo);
        PageInfo<MenuVo> pageInfo = new PageInfo<MenuVo>(list);
        int totalCnt = new Long(pageInfo.getTotal()).intValue();
        Page<MenuVo> page = new Page<MenuVo>();
        page.setList(list);
        page.setTotalCount(totalCnt);
        return page;
    }

    //菜单树
    public List<MenuVo> queryMenuTree() {
        MenuExample example = new MenuExample();
        example.setOrderByClause(" parent_id asc , sort asc ");
        List<Menu> list = menuMapper.selectByExample(example);
        List<MenuVo> midList =TransUtil.transList(list,MenuVo.class);
        List<MenuVo> rtList = genTree(midList);
        return  rtList;
    }

    public List<MenuVo> genTree(List<MenuVo> list) {
        if(list==null || list.size()<=0) {
            return null;
        }
        List<MenuVo> rtList = new ArrayList<MenuVo>();
        for (MenuVo menu : list) {
            if (menu.getParentId() == null || menu.getParentId() <= 0) {
                rtList.add(appendChildren(menu, list));
            }
        }
        return rtList;
    }

    private MenuVo appendChildren(MenuVo parentMenu, List<MenuVo> list) {
        for (MenuVo per : list) {
            if (per.getParentId() != null && per.getParentId().longValue() == parentMenu.getId()) {
                parentMenu.getChildren().add(appendChildren(per, list));
            }
        }
        return parentMenu;
    }

    //查询用户可见的菜单
    public List<MenuVo> queryUserMenu(Long userId) {
        List<MenuVo> list = menuDao.queryUserMenu(userId);
        List<MenuVo> rtList = genTree(list);
        return  rtList;
    }

    //查询类型是菜单的菜单列表
    public List<MenuVo> queryOnlyMenuList() {
        List<MenuVo> list = menuDao.queryOnlyMenuList();
        List<MenuVo> rtList = genTree(list);
        return  rtList;
    }

    //是否还有子菜单
    public boolean hasChildren(Long pMenuId) {
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(pMenuId);
        int cnt = menuMapper.countByExample(example);
        boolean has = (cnt>0);
        return has;
    }

    //批量删除菜单
    public Integer batchDel(List<Long> menuIds) {
        if(menuIds==null || menuIds.size()<=0) {
            return 0;
        }
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(menuIds);
        int cnt = menuMapper.deleteByExample(example);
        return cnt;
    }
}
