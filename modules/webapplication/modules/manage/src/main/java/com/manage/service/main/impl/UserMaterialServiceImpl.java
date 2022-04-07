package com.manage.service.main.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.EntityDao;
import com.manage.dao.ibatis.main.UserMaterialDao;
import com.manage.model.main.UserMaterial;
import com.manage.model.main.query.UserMaterialQuery;
import com.manage.service.BaseService;
import com.manage.service.main.UserMaterialService;

@Service
public class UserMaterialServiceImpl extends BaseService<UserMaterial,String> implements UserMaterialService {

	@Autowired
	private UserMaterialDao userMaterialDao;

	public UserMaterialDao getUserMaterialDao() {
		return userMaterialDao;
	}

	public void setUserMaterialDao(UserMaterialDao userMaterialDao) {
		this.userMaterialDao = userMaterialDao;
	}

	@Override
	protected EntityDao<UserMaterial, String> getEntityDao() {
		return userMaterialDao;
	}
	
	public Page<UserMaterial> findPage(UserMaterialQuery query) {
		return userMaterialDao.findPage(query);
	}

}
