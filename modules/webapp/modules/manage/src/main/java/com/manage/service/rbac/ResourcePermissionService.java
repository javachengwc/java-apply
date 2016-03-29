package com.manage.service.rbac;

import java.util.List;
import java.util.Map;

import com.manage.model.dto.ResourcePermissionDto;
import com.manage.model.rbac.ResourcePermission;

public interface ResourcePermissionService {
	
	public ResourcePermission getById(Integer id);

	public List<ResourcePermission> findByRole(Integer id);
	
	public List<ResourcePermission> findByResource(Integer id);
	
	public void save(ResourcePermission rp);
	
	public void update(ResourcePermission rp);
	
	public void removeById(Integer id);
	
	public void cleanByResource(Integer id);
	
	public void cleanByRole(Integer id);
	
	public void authByRole(Integer roleId,Map<Integer,Long> rpMap);

	public List<ResourcePermissionDto> getResourcePermissionDtoAll();
	
}

