package com.shop.book.manage.dao;

import com.shop.book.manage.model.vo.RoleQueryVo;
import com.shop.book.manage.model.vo.RoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {

    public List<RoleVo> queryByUserId(@Param("userId") Long userId);

    public List<RoleVo> queryPage(RoleQueryVo queryVo);
}
