package com.manage.service.rbac.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manage.dao.EntityDao;
import com.manage.dao.ibatis.rbac.ResourcePermissionDao;
import com.manage.model.dto.ResourcePermissionDto;
import com.manage.model.rbac.ResourcePermission;
import com.manage.service.BaseService;
import com.manage.service.rbac.AdminorHolder;
import com.manage.service.rbac.ResourcePermissionService;

@Service
public class ResourcePermissionServiceImpl extends BaseService<ResourcePermission,Integer> implements ResourcePermissionService {
	
	@Autowired
	private ResourcePermissionDao resourcePermissionDao;
	

	public ResourcePermissionDao getResourcePermissionDao() {
		return resourcePermissionDao;
	}

	public void setResourcePermissionDao(ResourcePermissionDao resourcePermissionDao) {
		this.resourcePermissionDao = resourcePermissionDao;
	}

	@Override
	protected EntityDao<ResourcePermission, Integer> getEntityDao() {
		return resourcePermissionDao;
	}

	@Override
	public List<ResourcePermission> findByRole(Integer id) {
		ResourcePermission rp=new ResourcePermission();
		rp.setRoleid(id);
		return resourcePermissionDao.findByRp(rp);
	}

	@Override
	public List<ResourcePermission> findByResource(Integer id) {
		ResourcePermission rp=new ResourcePermission();
		rp.setResourceid(id);
		return resourcePermissionDao.findByRp(rp);
	}
	
	public void cleanByResource(Integer id)
	{
		ResourcePermission rp=new ResourcePermission();
		rp.setResourceid(id);
		resourcePermissionDao.clean(rp);
	}
	public void cleanByRole(Integer id)
	{
		ResourcePermission rp=new ResourcePermission();
		rp.setRoleid(id);
		resourcePermissionDao.clean(rp);
	}
	
	@Transactional
	public void authByRole(Integer roleId,Map<Integer,Long> rpMap)
	{
		ResourcePermission rpRole=new ResourcePermission();
		rpRole.setRoleid(roleId);
		if(rpMap==null || rpMap.size()==0)
		{
			resourcePermissionDao.clean(rpRole);
		}else
		{
			List<ResourcePermission> rps=resourcePermissionDao.findByRp(rpRole);
			
			for(ResourcePermission perRp:rps)
			{
				if(!rpMap.keySet().contains(perRp.getResourceid()))
				{
					resourcePermissionDao.deleteById(perRp.getId());
				}
			}
		}
		for(Entry<Integer,Long> entry:rpMap.entrySet())
		{
			ResourcePermission rpCondition =new ResourcePermission();
			rpCondition.setRoleid(roleId);
			rpCondition.setResourceid(entry.getKey());
			
			ResourcePermission rp=resourcePermissionDao.getResourcePermissionByRR(rpCondition);
			
			if(rp==null)
			{
			     rpCondition.setFlag(entry.getValue());	
			     resourcePermissionDao.save(rpCondition);
			}
			else
			{
				if(rp.getFlag()!=entry.getValue().longValue())
				{
				    rp.setFlag(entry.getValue());
				    resourcePermissionDao.update(rp);
				}
			}
			
		}
		AdminorHolder.getInstance().init();
	}
	
	public List<ResourcePermissionDto> getResourcePermissionDtoAll()
	{
		return resourcePermissionDao.getResourcePermissionDtoAll();
	}
	
}