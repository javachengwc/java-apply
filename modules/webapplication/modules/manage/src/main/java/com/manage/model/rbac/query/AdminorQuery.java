package com.manage.model.rbac.query;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.manage.model.BaseQuery;
import com.manage.model.rbac.Adminor;

public class AdminorQuery  extends BaseQuery implements Serializable {
	
	private static final long serialVersionUID = 314817676855923L;
	
	private Adminor adminor=new Adminor();

	public Adminor getAdminor() {
		return adminor;
	}

	public void setAdminor(Adminor adminor) {
		this.adminor = adminor;
	}
	
	
	public Integer getId() {
	    return adminor.getId();
    }

    public void setId(Integer id) {
	    adminor.setId(id);
    }


    public String getName() {
	    return adminor.getName();
    }

    public void setName(String name) {
	    adminor.setName(name);
    }

    public String getPassword() {
	    return adminor.getPassword();
    }

    public void setPassword( String password) {
	    adminor.setPassword(password);
    }

    public String getNickname() {
	    return adminor.getNickname();
    }

    public void setNickname(String nickname) {
	    adminor.setNickname(nickname);
    }
	
    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
