package com.shop.book.manage.service.rdbc.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.base.model.Page;
import com.shop.base.util.TransUtil;
import com.shop.book.manage.dao.MenuDao;
import com.shop.book.manage.dao.mapper.MenuMapper;
import com.shop.book.manage.model.pojo.Menu;
import com.shop.book.manage.model.pojo.MenuExample;
import com.shop.book.manage.model.vo.MenuQueryVo;
import com.shop.book.manage.model.vo.MenuVo;
import com.shop.book.manage.service.rdbc.MenuService;
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
        List<MenuVo> rtList = genTree(list);
        return  rtList;
    }

    public List<MenuVo> genTree(List<Menu> list) {
        if(list==null || list.size()<=0) {
            return null;
        }
        List<MenuVo> rtList = new ArrayList<MenuVo>();
        for (Menu menu : list) {
            if (menu.getParentId() == null || menu.getParentId() <= 0) {
                MenuVo vo = TransUtil.transEntity(menu,MenuVo.class);
                rtList.add(appendChildren(vo, list));
            }
        }
        return rtList;
    }

    private MenuVo appendChildren(MenuVo parentMenu, List<Menu> list) {
        for (Menu per : list) {
            if (per.getParentId() != null && per.getParentId().longValue() == parentMenu.getId()) {
                MenuVo vo = TransUtil.transEntity(per,MenuVo.class);
                parentMenu.getChildren().add(appendChildren(vo, list));
            }
        }
        return parentMenu;
    }
}
