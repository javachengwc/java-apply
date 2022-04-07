package com.manage.web;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.manage.model.rbac.Adminor;
import com.manage.model.rbac.Module;
import com.manage.service.rbac.AdminorService;
import com.manage.service.rbac.ModuleService;

@Namespace("/")
@Results( { @Result(name = MainAction.RESULT, location = "main.jsp", type = "dispatcher")})
public class MainAction extends CrudActionSupport{
    
	private static final long serialVersionUID = -1239036080797506064L;

	public static final String RESULT="result";
	
	private Integer id;
	
	private List<Module> modules;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private AdminorService adminorService;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public ModuleService getModuleService() {
		return moduleService;
	}

	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	public AdminorService getAdminorService() {
		return adminorService;
	}

	public void setAdminorService(AdminorService adminorService) {
		this.adminorService = adminorService;
	}

	@Override
	protected void prepareModel() throws Exception {
		
	}
	
	public Object getModel() {
		return null;
	}

	@Override
	public String list() throws Exception {
		
        Integer adminorId=(Integer)this.getRequest().getSession().getAttribute("userId");
        
        if(adminorId==null)
			return SUCCESS;
		Adminor adminor=adminorService.getById(adminorId);
	    if(adminor==null)
	    	return SUCCESS;
	    if(adminor.isSuper())
	    {
	    	modules=moduleService.findAll();
	    }
	    else
	    {
	    	modules=moduleService.getAccessModulesByAdminor(adminor);
	    }
		return SUCCESS;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}
    
}
