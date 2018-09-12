package com.entity;
import org.apache.commons.lang.builder.ToStringBuilder;


public class Entity {
	
	private Integer id;
	
	private String name;

	private boolean good;

	public Entity() {

	}

	public Entity(Integer id,String name) {
		this.id=id;
		this.name=name;
	}

	public Entity(Integer id,String name,boolean good) {
		this(id,name);
		this.good=good;
	}

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

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}

}
