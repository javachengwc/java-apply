package com.manage.model.rbac.query;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.manage.model.BaseQuery;
import com.manage.model.rbac.Resource;

public class ResourceQuery  extends BaseQuery implements Serializable {
	
	private static final long serialVersionUID = 31481767633855923L;
	
	private Resource resource=new Resource(true);

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	
	public Integer getId() {
	    return resource.getId();
    }

    public void setId(Integer id) {
    	resource.setId(id);
    }


    public String getName() {
	    return resource.getName();
    }

    public void setName(String name) {
    	resource.setName(name);
    }

    public String getValue() {
	    return resource.getValue();
    }

    public void setValue( String value) {
    	resource.setValue(value);
    }
    
    public Integer getModuleid() {
		return resource.getModuleid();
	}
	
	public void setModuleid(Integer moduleid) {
		resource.setModuleid(moduleid);
	}
    
    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
