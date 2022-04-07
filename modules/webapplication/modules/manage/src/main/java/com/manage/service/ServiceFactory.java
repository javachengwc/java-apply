package com.manage.service;

import com.manage.service.rbac.AdminorService;
import com.manage.service.rbac.ResourcePermissionService;
import com.manage.service.rbac.RoleService;
import com.manage.util.SpringContextUtils;

public class ServiceFactory {

	public static AdminorService getAdminorService()
	{
		return SpringContextUtils.getBean("adminorServiceImpl",AdminorService.class);
	}
	
	public static ResourcePermissionService getResourcePermissionService()
	{
		return SpringContextUtils.getBean("resourcePermissionServiceImpl",ResourcePermissionService.class);
	}
	
	public static RoleService getRoleService()
	{
		return  SpringContextUtils.getBean("roleServiceImpl",RoleService.class);
	}

}
