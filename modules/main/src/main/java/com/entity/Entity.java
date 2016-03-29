package com.entity;
import org.apache.commons.lang.builder.ToStringBuilder;


public class Entity {
	
	private Integer id;
	
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}

}
