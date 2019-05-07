package com.storefront.manage.dao.rbac;

import com.storefront.manage.model.vo.MenuQueryVo;
import com.storefront.manage.model.vo.MenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDao {

    public List<MenuVo> queryPage(MenuQueryVo queryVo);

    public List<MenuVo> queryUserMenu(@Param("userId") Long userId);

    //查询类型是目录或菜单的菜单列表
    public List<MenuVo> queryOnlyMenuList();
}
