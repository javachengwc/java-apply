package com.manage.dao.ibatis.rbac;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.ibatis.BaseIbatisDao;
import com.manage.dao.ibatis.BaseKeyGenerator;
import com.manage.dao.ibatis.IbatisPage;
import com.manage.model.rbac.Resource;
import com.manage.model.rbac.query.ResourceQuery;

@Repository
public class ResourceDao extends RbacDao<Resource, Integer> {
	

	@Override
	public Class<Resource> getEntityClass() {
		return Resource.class;
	}
	
	public Page<Resource> findPage(ResourceQuery query)
	{
		return new IbatisPage<Resource>(this.getSqlMapClient(),BaseKeyGenerator.generatePageTotalCount(getEntityClass()),BaseKeyGenerator.generateFindPage(getEntityClass()),query);
	}
	
	@SuppressWarnings("unchecked")
	public List<Resource> findListByMd(Integer moduleId)
	{
		String queryStr="findListByMd";
		return (List<Resource>)this.getSqlMapClientTemplate().queryForList(queryStr, moduleId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Resource> getResourcesRelaAdminor(Integer adminorId)
	{
		String queryStr="getResourcesRelaAdminor";
		return (List<Resource>)this.getSqlMapClientTemplate().queryForList(queryStr, adminorId);
	}
}
