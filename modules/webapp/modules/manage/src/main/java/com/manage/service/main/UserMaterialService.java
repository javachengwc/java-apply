package com.manage.service.main;

import cn.org.rapid_framework.page.Page;

import com.manage.model.main.UserMaterial;
import com.manage.model.main.query.UserMaterialQuery;

public interface UserMaterialService {
	
    public UserMaterial getById(String id);
	
	public Page<UserMaterial> findPage(UserMaterialQuery query);
	
	public void save(UserMaterial userMaterial);
	
	public void update(UserMaterial userMaterial);
	
	public void removeById(String id);

}
