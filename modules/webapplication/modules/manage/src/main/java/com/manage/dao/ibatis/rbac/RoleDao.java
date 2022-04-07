package com.manage.dao.ibatis.rbac;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.ibatis.BaseIbatisDao;
import com.manage.dao.ibatis.BaseKeyGenerator;
import com.manage.dao.ibatis.IbatisPage;
import com.manage.model.rbac.Role;
import com.manage.model.rbac.query.RoleQuery;

@Repository
public class RoleDao extends RbacDao<Role, Integer> {
	

	@Override
	public Class<Role> getEntityClass() {
		return Role.class;
	}
	
	public Page<Role> findPage(RoleQuery query)
	{
		return new IbatisPage<Role>(this.getSqlMapClient(),BaseKeyGenerator.generatePageTotalCount(getEntityClass()),BaseKeyGenerator.generateFindPage(getEntityClass()),query);
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getListByAdminorId(Integer adminorId)
	{
		String queryStr="getRoleListByAdm";
		return (List<Role>)this.getSqlMapClientTemplate().queryForList(queryStr, adminorId);
	}
	
}