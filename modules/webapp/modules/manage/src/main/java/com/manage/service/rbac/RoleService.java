package com.manage.service.rbac;

import java.util.List;

import cn.org.rapid_framework.page.Page;

import com.manage.model.rbac.Role;
import com.manage.model.rbac.query.RoleQuery;

public interface RoleService {
	
	public Role getById(Integer id);

	public List<Role> findAll();
	
	public Page<Role> findPage(RoleQuery query);
	
	public void save(Role role);
	
	public void update(Role role);
	
	public void removeRole(Integer id);
	
	public List<Role> getAdminorRelaRoles(Integer adminorId);
}
