package com.manage.service.main;

import java.util.List;

import cn.org.rapid_framework.page.Page;

import com.manage.model.main.MaterialExt;
import com.manage.model.main.query.MaterialExtQuery;

public interface MaterialExtService {
	
	public List<MaterialExt> findAll();
	
	public Page<MaterialExt> findPage(MaterialExtQuery query);
	
	public void save(MaterialExt role);
	
	public void update(MaterialExt role);
	
    public boolean presentMaterial(String playerId,String combinid,int count);    
}
