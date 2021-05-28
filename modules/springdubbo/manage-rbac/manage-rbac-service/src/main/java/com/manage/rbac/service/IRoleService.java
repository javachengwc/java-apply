package com.manage.rbac.service;

import com.manage.rbac.entity.Role;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.RoleDTO;
import com.manage.rbac.model.dto.RoleMenuDTO;
import com.model.base.PageVo;

import java.util.List;

/**
 * Role的服务接口
 */
public interface IRoleService {

    //分页查询角色
    public PageVo<RoleDTO> listRolePage(Integer pageIndex, Integer pageSize);

    //根据名称查询数量
    public long countByName(String name);

    //根据编码查询数量
    public long countByCode(String code);

    //添加角色
    public boolean addRole(RoleDTO roleDTO);

    //更新角色
    public boolean updateRole(RoleDTO roleDTO);

    //启用角色
    public boolean enableRole(Integer id,OperatorDTO operator);

    //禁用角色
    public boolean disableRole(Integer id,OperatorDTO operator);

    //查询可选角色
    public List<Role> listOptionalRole();

    //修改角色菜单
    public boolean updateRoleMenu(RoleMenuDTO roleMenuDTO);

    //查询岗位的可选角色列表
    public List<Role> listOptionalRoleByPost(Integer postId);

    //查询用户组可选角色列表
    public List<Role> listOptionalRoleByCrowd(Integer crowdId);

    //查询岗位关联的角色
    public List<Role> listRoleByPost(Integer postId);

    //查询用户组关联的角色
    public List<Role> listRoleByCrowd(Integer crowdId);

    //根据用户岗位查询用户拥有的系统角色数量
    public long countSysRoleByUserPost(Integer userId);

    //根据用户用户组查询用户拥有的系统角色数量
    public long countSysRoleByUserCrowd(Integer userId);

    long countOrgAdminRoleByUserPost(Integer id);

    long countOrgAdminRoleByUserCrowd(Integer id);

    Role getById(Integer id);
}
