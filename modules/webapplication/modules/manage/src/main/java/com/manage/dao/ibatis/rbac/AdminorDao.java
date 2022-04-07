package com.manage.dao.ibatis.rbac;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.ibatis.BaseIbatisDao;
import com.manage.dao.ibatis.BaseKeyGenerator;
import com.manage.dao.ibatis.IbatisPage;
import com.manage.model.rbac.Adminor;
import com.manage.model.rbac.query.AdminorQuery;

@Repository
public class AdminorDao extends RbacDao<Adminor, Integer> {
	

	@Override
	public Class<Adminor> getEntityClass() {
		return Adminor.class;
	}
	
	public Page<Adminor> findPage(AdminorQuery query)
	{
		return new IbatisPage<Adminor>(this.getSqlMapClient(),BaseKeyGenerator.generatePageTotalCount(getEntityClass()),BaseKeyGenerator.generateFindPage(getEntityClass()),query);
	}
	
	public void addRole(Integer adminorId,Integer roleId)
	{
		String exeStat="addRole";
		Map<String,Integer> paramsMap =new HashMap<String,Integer>();
		paramsMap.put("adminorId", adminorId);
		paramsMap.put("roleId", roleId);
	    this.getSqlMapClientTemplate().insert(exeStat, paramsMap);	
	}
	public void cancelRole(Integer adminorId,Integer roleId)
	{
		String exeStat="cancelRole";
		Map<String,Integer> paramsMap =new HashMap<String,Integer>();
		paramsMap.put("adminorId", adminorId);
		paramsMap.put("roleId", roleId);
		this.getSqlMapClientTemplate().delete(exeStat, paramsMap);
	}
	
	public Adminor getByName(String name)
	{
		String queryStr="getByName";
		return (Adminor)this.getSqlMapClientTemplate().queryForObject(queryStr, name);
	}
	
}
