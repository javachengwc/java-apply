package com.manage.service.rbac;

import java.util.List;

import cn.org.rapid_framework.page.Page;

import com.manage.model.dto.ResourcePermissionDto;
import com.manage.model.rbac.Adminor;
import com.manage.model.rbac.Resource;
import com.manage.model.rbac.query.ResourceQuery;

public interface ResourceService {
	
	public Resource getById(Integer id);

	public List<Resource> findAll();
	
	public Page<Resource> findPage(ResourceQuery query);
	
	public Page<Resource> findHierarchyPage(ResourceQuery query);
	
	public void save(Resource resource);
	
	public void update(Resource resource);
	
	public void removeById(Integer id);
	
	public void removeResource(Integer id);
	
	public List<Resource> getResourcesByMd(Integer moduleId);
	
	public List<ResourcePermissionDto> findRpFullByRole(Integer roleId);
	
	public List<Resource> getAccessResourcesByAdminor(Adminor adminor);
}
