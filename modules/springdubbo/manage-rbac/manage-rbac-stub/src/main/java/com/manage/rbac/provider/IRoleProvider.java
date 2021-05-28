package com.manage.rbac.provider;

import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.RoleDTO;
import com.manage.rbac.model.dto.RoleMenuDTO;
import com.model.base.PageVo;
import com.model.base.Resp;

import java.util.List;

public interface IRoleProvider {

    //分页查询角色
    public Resp<PageVo<RoleDTO>> listRolePage(Integer pageIndex, Integer pageSize);

    //根据名称查询数量
    public Resp<Long> countByName(String name);

    //查询角色信息
    public Resp<RoleDTO> getRole(Integer id);

    //根据编码查询数量
    public Resp<Long> countByCode(String code);

    //增加角色
    public Resp<Void> addRole(RoleDTO roleDTO);

    //修改角色
    public Resp<Void> updateRole(RoleDTO roleDTO);

    //启用角色
    public Resp<Void> enableRole(Integer id, OperatorDTO operator);

    //禁用角色
    public Resp<Void> disableRole(Integer id,OperatorDTO operator);

    //查询可选角色
    public Resp<List<RoleDTO>> listOptionalRole();

    //修改角色菜单
    public Resp<Void> updateRoleMenu(RoleMenuDTO roleMenuDTO);

    public Resp<RoleDTO> getById(Integer id);

}