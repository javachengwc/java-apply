package com.shop.book.manage.dao;

import com.shop.book.manage.model.vo.MenuQueryVo;
import com.shop.book.manage.model.vo.MenuVo;

import java.util.List;

public interface MenuDao {

    public List<MenuVo> queryPage(MenuQueryVo queryVo);
}
