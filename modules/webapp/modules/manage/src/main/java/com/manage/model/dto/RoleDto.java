package com.manage.model.dto;

import com.manage.model.rbac.Role;

public class RoleDto {

	private Role role;
	
	private boolean rela =false;
	
	public  RoleDto()
	{
		
	}
	public RoleDto(Role role)
	{
		this.role =role;
	}
	public RoleDto(Role role,boolean rela)
	{
		this(role);
		this.rela=rela;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public boolean isRela() {
		return rela;
	}
	public void setRela(boolean rela) {
		this.rela = rela;
	}
	
	public Integer getId()
	{
		return role.getId();
	}
	
	public String getName()
	{
		return role.getName();
	}
	
	public String getIntroduct()
	{
		return role.getIntroduct();
	}
}
