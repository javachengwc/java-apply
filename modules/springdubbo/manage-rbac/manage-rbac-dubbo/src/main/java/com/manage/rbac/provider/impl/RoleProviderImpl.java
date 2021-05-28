package com.manage.rbac.provider.impl;

import com.model.base.PageVo;
import com.model.base.Resp;
import com.manage.rbac.model.common.Constant;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.RoleMenuDTO;
import com.manage.rbac.provider.IRoleProvider;
import com.manage.rbac.service.IRoleService;
import com.manage.rbac.model.dto.RoleDTO;
import com.manage.rbac.entity.Role;

import com.util.TransUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* Role相关dubbo服务实现类.
*
* @author
*/
@Service(version = Constant.DUBBO_API_VERSION)
public class RoleProviderImpl implements IRoleProvider {

    @Autowired
    private IRoleService service;
    //分页查询角色
    @Override
    public Resp<PageVo<RoleDTO>> listRolePage(Integer pageIndex, Integer pageSize) {
        PageVo<RoleDTO> pageData = service.listRolePage(pageIndex,pageSize);
        return Resp.data(pageData);
    }

    //根据名称查询数量
    @Override
    public Resp<Long> countByName(String name) {
        long count = service.countByName(name);
        return Resp.data(count);
    }

    //查询角色信息
    @Override
    public Resp<RoleDTO> getRole(Integer id) {
       Role role=service.getById(id);
       if(role==null) {
           return Resp.success();
       }
       RoleDTO roleDTO = TransUtil.transEntity(role,RoleDTO.class);
       return Resp.data(roleDTO);
    }

    //根据编码查询数量
    @Override
    public Resp<Long> countByCode(String code) {
        long count = service.countByCode(code);
        return Resp.data(count);
    }

    //增加角色
    @Override
    public Resp<Void> addRole(RoleDTO roleDTO) {
        service.addRole(roleDTO);
        return Resp.success();
    }

    //修改角色
    @Override
    public Resp<Void> updateRole(RoleDTO roleDTO) {
        service.updateRole(roleDTO);
        return Resp.success();
    }

    //启用角色
    @Override
    public Resp<Void> enableRole(Integer id, OperatorDTO operator) {
        service.enableRole(id,operator);
        return Resp.success();
    }

    //禁用角色
    @Override
    public Resp<Void> disableRole(Integer id,OperatorDTO operator) {
        service.disableRole(id,operator);
        return Resp.success();
    }

    //查询可选角色
    @Override
    public Resp<List<RoleDTO>> listOptionalRole() {
        List<Role> list =service.listOptionalRole();
        List<RoleDTO> rtList = TransUtil.transList(list,RoleDTO.class);
        return Resp.data(rtList);
    }

    //修改角色菜单
    @Override
    public Resp<Void> updateRoleMenu(RoleMenuDTO roleMenuDTO) {
        service.updateRoleMenu(roleMenuDTO);
        return Resp.success();
    }

    //查询岗位的可选角色列表
    public Resp<List<RoleDTO>> listOptionalRoleByPost(Integer postId) {
        List<Role> list =service.listOptionalRoleByPost(postId);
        List<RoleDTO> rtList = TransUtil.transList(list,RoleDTO.class);
        return Resp.data(rtList);
    }

    //查询用户组可选角色列表
    public Resp<List<RoleDTO>> listOptionalRoleByCrowd(Integer crowdId) {
        List<Role> list =service.listOptionalRoleByCrowd(crowdId);
        List<RoleDTO> rtList = TransUtil.transList(list,RoleDTO.class);
        return Resp.data(rtList);
    }

    @Override
    public Resp<RoleDTO> getById(Integer id) {
        Role role = service.getById(id);
        RoleDTO dto = TransUtil.transEntity(role,RoleDTO.class);
        return Resp.data(dto);
    }
}