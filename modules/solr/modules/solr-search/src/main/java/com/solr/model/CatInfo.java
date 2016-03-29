package com.solr.model;
public class CatInfo {
	private String cat_id;
	private String cat_name;
	private String goods_num;
	private String parent_id;
	private String pinyin;
	private String show_type;
	private String cat_url;
	private int count;
	public CatInfo(){
		
	}
	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	public String getCat_name() {
		return cat_name;
	}

	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}

	public String getGoods_num() {
		return goods_num;
	}

	public void setGoods_num(String goods_num) {
		this.goods_num = goods_num;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getShow_type() {
		return show_type;
	}

	public void setShow_type(String show_type) {
		this.show_type = show_type;
	}
	public final int getCount() {
		return count;
	}
	public final void setCount(int count) {
		this.count = count;
	}
	public final String getCat_url() {
		return cat_url;
	}
	public final void setCat_url(String cat_url) {
		this.cat_url = cat_url;
	}
	@Override
	public String toString() {
		return "CatInfo [cat_name=" + cat_name + ", parent_id=" + parent_id
				+ "]";
	}
}