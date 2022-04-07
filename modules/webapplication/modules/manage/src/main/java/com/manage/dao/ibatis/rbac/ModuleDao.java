package com.manage.dao.ibatis.rbac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.ibatis.BaseIbatisDao;
import com.manage.dao.ibatis.BaseKeyGenerator;
import com.manage.dao.ibatis.IbatisPage;
import com.manage.model.rbac.Module;
import com.manage.model.rbac.query.ModuleQuery;

@Repository
public class ModuleDao extends RbacDao<Module, Integer> {
	
	@Override
	public Class<Module> getEntityClass() {
		return Module.class;
	}
	
	public Page<Module> findPage(ModuleQuery query)
	{
		return new IbatisPage<Module>(this.getSqlMapClient(),BaseKeyGenerator.generatePageTotalCount(getEntityClass()),BaseKeyGenerator.generateFindPage(getEntityClass()),query);
	}
	
	public Page<Module> findHierarchyPage(ModuleQuery query)
	{
		String queryPageCountStr="findPageHiModuleTotalCount";
		String queryPageListStr="findPageHiModule";
	
		return new IbatisPage<Module>(this.getSqlMapClient(),queryPageCountStr,queryPageListStr,query);
	}
	
    @SuppressWarnings("unchecked")
	public List<Module> getChildsById(Integer id)
    {
    	String queryStr="getChildsById";
    	Map<String,Integer> paramsMap = new HashMap<String,Integer>();
    	paramsMap.put("id", id);
    	return (List<Module>)this.getSqlMapClientTemplate().queryForList(queryStr, paramsMap);
    }
	
}
