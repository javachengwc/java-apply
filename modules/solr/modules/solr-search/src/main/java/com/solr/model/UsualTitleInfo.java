package com.solr.model;
/**
 * 存储title信息的对象
 */
public class UsualTitleInfo {
	
	private String keywords;
	private String catName;
	private String description;
	private String style;
	private String grade;
	private String filter_attr;
	private String parent_id;
	private String cat_id;
	
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getFilter_attr() {
		return filter_attr;
	}
	public void setFilter_attr(String filterAttr) {
		filter_attr = filterAttr;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parentId) {
		parent_id = parentId;
	}
	public String getCat_id() {
		return cat_id;
	}
	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}
}
