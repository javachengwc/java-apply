package com.storefront.manage.dao.rbac;

import com.storefront.manage.model.pojo.rbac.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleDao {

    public Integer queryUserRoleCountByRole(@Param("roleId") Long roleId);

    //查询用户的角色列表
    public List<Role> queryRoleByUser(@Param("userId") Long userId);

    public Integer deleteByUser(@Param("userId") Long userId);

    //增加用户角色
    public Integer addUserRoles(@Param("userId") Long userId, @Param("roleIds") Long[] roleIds);
}
