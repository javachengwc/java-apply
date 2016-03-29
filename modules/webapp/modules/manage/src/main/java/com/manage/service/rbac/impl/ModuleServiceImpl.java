package com.manage.service.rbac.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.EntityDao;
import com.manage.dao.ibatis.rbac.ModuleDao;
import com.manage.model.rbac.Adminor;
import com.manage.model.rbac.Module;
import com.manage.model.rbac.Resource;
import com.manage.model.rbac.query.ModuleQuery;
import com.manage.service.BaseService;
import com.manage.service.rbac.ModuleService;
import com.manage.service.rbac.ResourceService;

@Service
public class ModuleServiceImpl extends BaseService<Module,Integer> implements ModuleService {
	
	@Autowired
	private ModuleDao moduleDao;
	
	@Autowired
	private ResourceService resourceService;
	
	public ModuleDao getModuleDao() {
		return moduleDao;
	}

	public void setModuleDao(ModuleDao moduleDao) {
		this.moduleDao = moduleDao;
	}
	
	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@Override
	protected EntityDao<Module, Integer> getEntityDao() {
		return moduleDao;
	}
	
	public List<Module> getPermissModule()
	{
		return moduleDao.findAll();
	}
	
	public List<Module> getChildsByParentId(Integer id)
	{
//		Module module=this.getById(id);
//		
//		return Arrays.asList(module.getChilds().toArray(new Module[module.getChilds().size()]));
		return moduleDao.getChildsById(id);
	}

	public Page findPage(ModuleQuery query) {
		return moduleDao.findPage(query);
	}

	public Page findHierarchyPage(ModuleQuery query)
	{
		return moduleDao.findHierarchyPage(query);
	}
	
	public List<Module> getAccessModulesByAdminor(Adminor adminor)
	{
		Set<Module> sets=new HashSet<Module>();
		List<Resource> resources=resourceService.getAccessResourcesByAdminor(adminor);
		for(Resource resource:resources)
		{
			if(resource.getModuleid()==null)
				continue;
			Module m=this.getById(resource.getModuleid());
			if(m!=null)
				sets.add(m);
			if(m.getParent()!=null)
				sets.add(m.getParent());
		}
	   return Arrays.asList(sets.toArray(new Module[sets.size()]));
	}
}
