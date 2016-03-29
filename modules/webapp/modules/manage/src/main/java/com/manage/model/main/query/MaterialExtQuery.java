package com.manage.model.main.query;

import java.io.Serializable;

import com.manage.model.BaseQuery;
import com.manage.model.main.MaterialExt;

public class MaterialExtQuery extends BaseQuery implements Serializable {
	
	private static final long serialVersionUID = 31481718855923L;
	
	private MaterialExt material=new MaterialExt();

	public MaterialExt getMaterial() {
		return material;
	}

	public void setMaterial(MaterialExt material) {
		this.material = material;
	}
	
	public int getId() {
		return material.getId();
	}

	public void setId(int id) {
		material.setId(id);
	}

	public String getCombinid() {
		return material.getCombinid();
	}

	public void setCombinid(String combinid) {
		material.setCombinid(combinid);
	}

	public String getName() {
	    return	material.getName();
	}

	public void setName(String name) {
		material.setName(name);
	}


	public int getType() {
		return material.getType();
	}

	public void setType(int type) {
		material.setType(type);
	}
}
