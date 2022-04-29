package com.commonservice.invoke.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.system.MenuMapper;
import com.commonservice.invoke.model.entity.system.Menu;
import com.commonservice.invoke.service.system.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> queryMenuTreeByUserId(Long userId) {
        List<Menu> list = this.list();
        List<Menu> rtList = this.genTree(list);
        return  rtList;
    }

    private List<Menu> genTree(List<Menu> list) {
        if(list==null || list.size()<=0) {
            return null;
        }
        List<Menu> rtList = new ArrayList<Menu>();
        for (Menu menu : list) {
            if (menu.getParentId() == null || menu.getParentId() <= 0) {
                rtList.add(this.appendChildren(menu, list));
            }
        }
        return rtList;
    }

    private Menu appendChildren(Menu parentMenu, List<Menu> list) {
        for (Menu per : list) {
            if (per.getParentId() != null && per.getParentId().intValue() == parentMenu.getId()) {
                parentMenu.getChildren().add(this.appendChildren(per, list));
            }
        }
        return parentMenu;
    }

}
