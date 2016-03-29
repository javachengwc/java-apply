package com.manage.dao.ibatis.main;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.ibatis.BaseIbatisDao;
import com.manage.dao.ibatis.BaseKeyGenerator;
import com.manage.dao.ibatis.IbatisPage;
import com.manage.model.main.UserMaterial;
import com.manage.model.main.query.UserMaterialQuery;

@Repository
public class UserMaterialDao extends MainDao<UserMaterial, String> {

	@Override
	public Class<UserMaterial> getEntityClass() {
		return UserMaterial.class;
	}
	
	public Page<UserMaterial> findPage(UserMaterialQuery query)
	{
		return new IbatisPage<UserMaterial>(this.getSqlMapClient(),BaseKeyGenerator.generatePageTotalCount(getEntityClass()),BaseKeyGenerator.generateFindPage(getEntityClass()),query);
	}
}