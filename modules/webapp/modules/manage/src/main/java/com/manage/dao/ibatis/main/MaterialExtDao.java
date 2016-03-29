package com.manage.dao.ibatis.main;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.ibatis.BaseIbatisDao;
import com.manage.dao.ibatis.BaseKeyGenerator;
import com.manage.dao.ibatis.IbatisPage;
import com.manage.model.main.MaterialExt;
import com.manage.model.main.query.MaterialExtQuery;

@Repository
public class MaterialExtDao extends MainDao<MaterialExt, String> {
	

	@Override
	public Class<MaterialExt> getEntityClass() {
		return MaterialExt.class;
	}
	
	public Page<MaterialExt> findPage(MaterialExtQuery query)
	{
		return new IbatisPage<MaterialExt>(this.getSqlMapClient(),BaseKeyGenerator.generatePageTotalCount(getEntityClass()),BaseKeyGenerator.generateFindPage(getEntityClass()),query);
	}
}