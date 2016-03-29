package com.manage.service.rbac;

import java.util.List;

import cn.org.rapid_framework.page.Page;

import com.manage.model.dto.RoleDto;
import com.manage.model.rbac.Adminor;
import com.manage.model.rbac.ResourcePermission.OperationFlag;
import com.manage.model.rbac.query.AdminorQuery;

public interface AdminorService {
	
	public Adminor getById(Integer id);
	
	public Adminor getByName(String name);

	public List<Adminor> findAll();
	
	public Page<Adminor> findPage(AdminorQuery query);
	
	public void save(Adminor adminor);
	
	public void update(Adminor adminor);
	
	public void removeAdminor(Integer id);
	
	public List<RoleDto> getRelaRoles(Integer id);
	
	public boolean assign(Adminor adminor,List<Integer> roleIds);
	
}
