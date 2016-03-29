package com.manage.service.rbac;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.xwork.StringUtils;

import com.manage.model.LongEnumSetTransform;
import com.manage.model.dto.ResourcePermissionDto;
import com.manage.model.rbac.Adminor;
import com.manage.model.rbac.ResourcePermission.OperationFlag;
import com.manage.model.rbac.Role;
import com.manage.service.ServiceFactory;

public class AdminorHolder {
	
	private static AdminorHolder inst=null;
	
	private AdminorHolder()
	{
		init();
	}
	public static synchronized AdminorHolder getInstance()
	{
		if(inst==null)
			inst =new AdminorHolder();
		return inst;
	}
	
	private static ThreadLocal<Integer> local=new ThreadLocal<Integer>();

	private static CopyOnWriteArrayList<ResourcePermissionDto> rpList=new CopyOnWriteArrayList<ResourcePermissionDto>();
	
	public synchronized void init()
	{
		rpList.clear();
		List<ResourcePermissionDto> list= ServiceFactory.getResourcePermissionService().getResourcePermissionDtoAll();
		System.out.println("rplist.size="+list.size());
		if(list!=null)
			rpList.addAll(list);
	}
	
	public static Integer getCurrentUserId()
	{
		return local.get();
	}
	public static void setCurrentUserId(Integer id)
	{
		local.set(id);
	}
	
	public static void cleanUserId()
	{
		local.remove();
	}
	
	public boolean hasPermission(String resource,OperationFlag needOp)
	{
		if(StringUtils.isBlank(resource) || needOp==null)
			return false;
		if(getCurrentUserId()==null)
			return false;
		Integer id =getCurrentUserId();
		AdminorService adminorService =ServiceFactory.getAdminorService();
		
		Adminor adminor =adminorService.getById(id);
		if(adminor==null)
			return false;
		if(adminor.isSuper())
			return true;
		
		List<Role> roles=ServiceFactory.getRoleService().getAdminorRelaRoles(id);
		if(roles==null || roles.size()==0)
			return false;
		if(rpList==null || rpList.size()==0)
	        return true;
		for(Role role:roles)
		{
			for(ResourcePermissionDto dto:rpList)
			{
				if(role.getId()==dto.getRoleid().intValue())
				{
					if(resource.equalsIgnoreCase(dto.getResourceValue()))
					{
						Long flag=dto.getFlag();
						EnumSet<OperationFlag> operations = EnumSet.noneOf(OperationFlag.class);
						operations=LongEnumSetTransform.long2EnumSet(OperationFlag.class, flag);
						if(operations.contains(needOp))
						{
							return true;
						}
					}	
				}
			}
		}
		return false;
	}
}
