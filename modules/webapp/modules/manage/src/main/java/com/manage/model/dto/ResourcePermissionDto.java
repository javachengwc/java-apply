package com.manage.model.dto;

import com.manage.model.rbac.Resource;
import com.manage.model.rbac.ResourcePermission;
import com.util.enh.BeanCopyUtil;

public class ResourcePermissionDto {

	private Integer id;
	
	private Integer roleid;
	
	private String roleName;
	
    private Integer resourceid;
   
    private Long flag;
    
    private String resourceValue;
    
    private String resourceIntroduct;
    
    public ResourcePermissionDto()
    {
    	
    }
    
    public ResourcePermissionDto(ResourcePermission  resourcePermission)
    {
        BeanCopyUtil.copyProperties(this,resourcePermission);
    }
    public ResourcePermissionDto(Resource resource)
    {
    	this.resourceid=resource.getId();
    	this.resourceValue=resource.getValue();
    	this.resourceIntroduct=resource.getIntroduct();
    }
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getResourceid() {
		return resourceid;
	}

	public void setResourceid(Integer resourceid) {
		this.resourceid = resourceid;
	}

	public Long getFlag() {
		return flag;
	}

	public void setFlag(Long flag) {
		this.flag = flag;
	}

	public String getResourceValue() {
		return resourceValue;
	}

	public void setResourceValue(String resourceValue) {
		this.resourceValue = resourceValue;
	}

	public String getResourceIntroduct() {
		return resourceIntroduct;
	}

	public void setResourceIntroduct(String resourceIntroduct) {
		this.resourceIntroduct = resourceIntroduct;
	}
    
    
    

}
