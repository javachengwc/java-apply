package com.shop.book.manage.dao.manage;

import com.shop.book.manage.model.vo.MenuQueryVo;
import com.shop.book.manage.model.vo.MenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDao {

    public List<MenuVo> queryPage(MenuQueryVo queryVo);

    public List<MenuVo> queryUserMenu(@Param("userId") Long userId);

    public List<MenuVo> queryUserMenuFeature(@Param("userId") Long userId);
}
