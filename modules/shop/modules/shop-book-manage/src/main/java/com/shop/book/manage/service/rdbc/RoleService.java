package com.shop.book.manage.service.rdbc;

import com.shop.base.model.Page;
import com.shop.book.manage.model.pojo.Role;
import com.shop.book.manage.model.vo.RoleQueryVo;
import com.shop.book.manage.model.vo.RoleVo;

import java.util.List;

public interface RoleService {

    public Role getById(Long id);

    public List<Role> queryAll();

    public Role queryByCode(String code);

    public Role queryByName(String name);

    public Role addRole(Role role);

    public Integer uptRole(Role role);

    public Integer delRoles(List<Long> roleIds);

    public Page<RoleVo> queryPage(RoleQueryVo queryVo);

    public boolean hasExistRoleName(String name);

    public boolean hasExistRoleCode(String code);

    public List<RoleVo> queryUserRole(Long userId);

}
