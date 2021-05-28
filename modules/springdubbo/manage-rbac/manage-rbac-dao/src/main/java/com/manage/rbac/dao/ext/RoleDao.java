package com.manage.rbac.dao.ext;

import com.manage.rbac.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {

    //分页查询角色
    public List<Role> listRolePage(@Param("start") Integer start,@Param("pageSize") Integer pageSize);

    //查询岗位关联的角色
    public List<Role> listRoleByPost(@Param("postId") Integer postId);

    //查询用户组关联的角色
    public List<Role> listRoleByCrowd(@Param("crowdId") Integer crowdId);

    //添加角色菜单
    public Integer addRoleMenu(@Param("roleId") Integer roleId,@Param("menuIdList") List<Integer> menuIdList);

    //根据角色删除角色菜单
    public Integer deleteRoleMenuByRole(@Param("roleId") Integer roleId);

    //查询岗位未关联的角色
    public List<Role> listRoleNotRelaPost(@Param("postId") Integer postId);

    //查询用户组未关联的角色
    public List<Role> listRoleNotRelaCrowd(@Param("crowdId") Integer crowdId);

    //根据用户岗位查询用户拥有的系统角色数量
    public long countSysRoleByUserPost(@Param("userId") Integer userId);

    //根据用户岗位查询用户拥有的系统角色数量
    public long countTeacherRoleByUserPost(@Param("userId") Integer userId);

    //根据用户用户组查询用户拥有的系统角色数量
    public long countSysRoleByUserCrowd(@Param("userId") Integer userId);

    //根据用户用户组查询用户拥有的班主任角色数量
    public long countTeacherRoleByUserCrowd(@Param("userId") Integer userId);

    long countNotTeacherRoleByUserPost(@Param("userId") Integer userId);

    long countNotTeacherByUserCrowd(@Param("userId") Integer id);

    long countOrgAdminRoleByUserPost(@Param("userId") Integer userId);

    long countOrgAdminRoleByUserCrowd(@Param("userId") Integer userId);
}
