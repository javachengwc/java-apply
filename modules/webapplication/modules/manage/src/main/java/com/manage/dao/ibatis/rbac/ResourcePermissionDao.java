package com.manage.dao.ibatis.rbac;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.manage.dao.ibatis.BaseIbatisDao;
import com.manage.model.dto.ResourcePermissionDto;
import com.manage.model.rbac.ResourcePermission;

@Repository
public class ResourcePermissionDao extends RbacDao<ResourcePermission, Integer> {
	

	@Override
	public Class<ResourcePermission> getEntityClass() {
		return ResourcePermission.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourcePermission> findByRp(ResourcePermission rp)
	{
		String queryStr="findByRp";
		return (List<ResourcePermission>)this.getSqlMapClientTemplate().queryForList(queryStr, rp);
	}
	
	public void clean(ResourcePermission rp)
	{
		String exeStr="cleanRp";
		this.getSqlMapClientTemplate().delete(exeStr,rp);
	}
	
	public ResourcePermission getResourcePermissionByRR(ResourcePermission rp)
	{
		String queryStr="getResourcePermissionByRR";
		return (ResourcePermission)this.getSqlMapClientTemplate().queryForObject(queryStr, rp);
	}

	@SuppressWarnings("unchecked")
	public List<ResourcePermissionDto>  getResourcePermissionDtoAll()
	{
		String queryStr="getResourcePermissionDtoAll";
		return (List<ResourcePermissionDto>)this.getSqlMapClientTemplate().queryForList(queryStr);
	}
}