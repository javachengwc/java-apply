package com.manage.service.rbac;

import java.util.List;

import cn.org.rapid_framework.page.Page;

import com.manage.model.rbac.Adminor;
import com.manage.model.rbac.Module;
import com.manage.model.rbac.query.ModuleQuery;

public interface ModuleService {
	
    public Module getById(Integer id);
    
    public List<Module> findAll();
	
    public List<Module> getPermissModule();
	
	public List<Module> getChildsByParentId(Integer id);
	
	public Page findPage(ModuleQuery query);
	
	public Page findHierarchyPage(ModuleQuery query);
	
	public void save(Module module);
	
	public void update(Module module);
	
	public void removeById(Integer id);
	
	public List<Module> getAccessModulesByAdminor(Adminor adminor);
    
}
