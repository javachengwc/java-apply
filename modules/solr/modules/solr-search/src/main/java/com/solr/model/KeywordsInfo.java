package com.solr.model;

/**
 * /keywords/ 对应的信息对象
 */
public class KeywordsInfo {
	
	private String keyword ;
	private String des ;
	private String related_keyword ;
	private String pinyin ;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getRelated_keyword() {
		return related_keyword;
	}
	public void setRelated_keyword(String relatedKeyword) {
		related_keyword = relatedKeyword;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
}
