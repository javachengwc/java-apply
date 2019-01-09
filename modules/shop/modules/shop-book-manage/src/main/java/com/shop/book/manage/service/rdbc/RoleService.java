package com.shop.book.manage.service.rdbc;

import com.shop.base.model.Page;
import com.shop.book.manage.model.pojo.manage.Role;
import com.shop.book.manage.model.vo.RoleQueryVo;
import com.shop.book.manage.model.vo.RoleVo;

import java.util.List;

public interface RoleService {

    public Role getById(Long id);

    public List<Role> queryAll();

    public Role queryByCode(String code);

    public Role queryByName(String name);

    public Role addRole(Role role);

    //添加角色，顺带添加角色菜单
    public Role addRoleWithMenu(RoleVo roleVo);

    public Integer uptRole(Role role);

    public Integer uptRoleWithMenu(RoleVo roleVo);

    public Integer delRoles(List<Long> roleIds);

    //删除角色，顺带删除角色菜单
    public Integer delRolesWithMenu(List<Long> roleIds);

    public Page<RoleVo> queryPage(RoleQueryVo queryVo);

    public boolean hasExistRoleName(String name);

    public boolean hasExistRoleCode(String code);

    public List<RoleVo> queryUserRole(Long userId);

    public boolean hasUser(Long roleId);

    //获取角色下的菜单ID
    public List<Long> queryMenuIdsByRole(Long roleId);

}
