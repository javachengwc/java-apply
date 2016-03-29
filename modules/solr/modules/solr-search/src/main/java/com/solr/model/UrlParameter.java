package com.solr.model;
/*
 * 每一个 service.cfg的field对应一个UrlParameter
 */
public class UrlParameter {
	
	private int index;
	private String name;
	private String solrname;
	private String expression;
	private String separtor;
	private String type;
	
	public final int getIndex() {
		return index;
	}
	public final void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSolrname() {
		return solrname;
	}
	public void setSolrname(String solrname) {
		this.solrname = solrname;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getSepartor() {
		return separtor;
	}
	public void setSepartor(String separtor) {
		this.separtor = separtor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
