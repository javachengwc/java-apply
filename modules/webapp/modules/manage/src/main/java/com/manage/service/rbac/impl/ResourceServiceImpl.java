package com.manage.service.rbac.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.EntityDao;
import com.manage.dao.ibatis.rbac.ResourceDao;
import com.manage.model.dto.ResourcePermissionDto;
import com.manage.model.rbac.Adminor;
import com.manage.model.rbac.Resource;
import com.manage.model.rbac.ResourcePermission;
import com.manage.model.rbac.query.ResourceQuery;
import com.manage.service.BaseService;
import com.manage.service.rbac.ResourcePermissionService;
import com.manage.service.rbac.ResourceService;

@Service
public class ResourceServiceImpl extends BaseService<Resource,Integer> implements ResourceService {
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private ResourcePermissionService resourcePermissionService;

	public ResourceDao getResourceDao() {
		return resourceDao;
	}

	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}
	

	public ResourcePermissionService getResourcePermissionService() {
		return resourcePermissionService;
	}

	public void setResourcePermissionService(
			ResourcePermissionService resourcePermissionService) {
		this.resourcePermissionService = resourcePermissionService;
	}

	@Override
	protected EntityDao<Resource, Integer> getEntityDao() {
		return resourceDao;
	}
	
	public Page<Resource> findPage(ResourceQuery query) {
		return resourceDao.findPage(query);
	}

	public List<Resource> getResourcesByMd(Integer moduleId)
	{
		return resourceDao.findListByMd(moduleId);
	}
	
	public Page<Resource> findHierarchyPage(ResourceQuery query)
	{
		return resourceDao.findPage(query);
	}
	
	@Transactional
	public void removeResource(Integer id)
	{
		resourcePermissionService.cleanByResource(id);
		this.removeById(id);
	}

	public List<ResourcePermissionDto> findRpFullByRole(Integer roleId)
	{
		List<ResourcePermissionDto> list =new ArrayList<ResourcePermissionDto>();
		List<Resource> rsList =this.findAll();
		List<ResourcePermission> rpList =resourcePermissionService.findByRole(roleId);
		for(Resource rs:rsList)
		{
			ResourcePermissionDto dto=new ResourcePermissionDto(rs);
			dto.setRoleid(roleId);
			for(ResourcePermission rp:rpList)
			{
				if(rp.getResourceid().intValue()==rs.getId())
				{
					dto.setFlag(rp.getFlag());
					break;
				}
			}
			list.add(dto);
		}
		return list;
	}
	
	public List<Resource> getAccessResourcesByAdminor(Adminor adminor)
	{
		return resourceDao.getResourcesRelaAdminor(adminor.getId());
	}
}
