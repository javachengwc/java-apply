package com.manage.model.rbac.query;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.manage.model.BaseQuery;
import com.manage.model.rbac.Role;

public class RoleQuery extends BaseQuery implements Serializable {
	
	private static final long serialVersionUID = 31481723676855923L;
	
	private Role role=new Role();

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	
	public Integer getId() {
	    return role.getId();
    }

    public void setId(Integer id) {
    	role.setId(id);
    }


    public String getName() {
	    return role.getName();
    }

    public void setName(String name) {
    	role.setName(name);
    }
    
    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}