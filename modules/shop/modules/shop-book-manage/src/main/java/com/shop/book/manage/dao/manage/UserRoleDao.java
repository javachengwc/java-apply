package com.shop.book.manage.dao.manage;

import org.apache.ibatis.annotations.Param;

public interface UserRoleDao {

    public Integer queryUserRoleCountByRole(@Param("roleId") Long roleId);
}
