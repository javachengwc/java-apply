package com.manage.service.rbac.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.EntityDao;
import com.manage.dao.ibatis.rbac.AdmRelaRoleDao;
import com.manage.dao.ibatis.rbac.RoleDao;
import com.manage.model.rbac.Role;
import com.manage.model.rbac.query.RoleQuery;
import com.manage.service.BaseService;
import com.manage.service.rbac.ResourcePermissionService;
import com.manage.service.rbac.RoleService;
@Service
public class RoleServiceImpl extends BaseService<Role,Integer> implements RoleService {
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private AdmRelaRoleDao admRelaRoleDao;
	
	@Autowired
	private ResourcePermissionService resourcePermissionService;
	
	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	
	public AdmRelaRoleDao getAdmRelaRoleDao() {
		return admRelaRoleDao;
	}

	public void setAdmRelaRoleDao(AdmRelaRoleDao admRelaRoleDao) {
		this.admRelaRoleDao = admRelaRoleDao;
	}

	public ResourcePermissionService getResourcePermissionService() {
		return resourcePermissionService;
	}

	public void setResourcePermissionService(
			ResourcePermissionService resourcePermissionService) {
		this.resourcePermissionService = resourcePermissionService;
	}

	@Override
	protected EntityDao<Role, Integer> getEntityDao() {
		return roleDao;
	}
	
	public Page<Role> findPage(RoleQuery query) {
		return roleDao.findPage(query);
	}

	public List<Role> getAdminorRelaRoles(Integer adminorId)
	{
		return roleDao.getListByAdminorId(adminorId);
	}
	
	@Transactional
	public void removeRole(Integer id)
	{
		resourcePermissionService.cleanByRole(id);
		
		Map<String,Integer> map =new HashMap<String,Integer>();
		map.put("roleId", id);
		admRelaRoleDao.releaseRela(map);
		
		this.removeById(id);
	}
}
