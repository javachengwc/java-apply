package com.solr.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;

public class FacetVo {
	
	private String count="";
	private String name="";
	private String url="";
	private String id="";
	
	private List<FacetVo> list=new LinkedList<FacetVo>();
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<FacetVo> getList() {
		return list;
	}
	public void setList(List<FacetVo> list) {
		this.list = list;
	}
	
	public void addFacet(FacetVo vo)
	{
		if(list==null)
		{
			list= new LinkedList<FacetVo>();
		}
		list.add(vo);
	}
	
	public void delFacet(FacetVo vo)
	{
		if(list!=null)
		{
			list.remove(vo);
		}
	}

	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
}
