package com.manage.model.rbac.query;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.manage.model.BaseQuery;
import com.manage.model.rbac.Module;

public class ModuleQuery extends BaseQuery implements Serializable {
	private static final long serialVersionUID = 3148176768559230877L;
	
	private Module module=new Module(true);
	
	public ModuleQuery()
	{
	}
	
	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Integer getId() {
		return module.getId();
	}
	public void setId(Integer id) {
		module.setId(id);
	}
	public String getName() {
		return module.getName();
	}
	public void setName(String name) {
		module.setName(name);
	}
	public String getUrl() {
		return module.getUrl();
	}
	public void setUrl(String url) {
		module.setUrl(url);
	}
	
	public Integer getParentid() {
		return getParent().getId();
	}
	public void setParentid(Integer parentid) {
		getParent().setId(parentid);
		module.setParentid(parentid);
	}
	
	public Module getParent() {
		return module.getParent();
	}
	public void setParent(Module parent) {
		module.setParent(parent);
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
