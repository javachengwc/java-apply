package com.solr.model;
/**
 * 分类广告图片及其链接
 */
public class CategoryAdImg {
	private String ad_img_id;
	private String goods_id;
	private String name;
	private String img;
	private String url;
	private String ad_img_desc;
	public String getAd_img_id() {
		return ad_img_id;
	}
	public void setAd_img_id(String ad_img_id) {
		this.ad_img_id = ad_img_id;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAd_img_desc() {
		return ad_img_desc;
	}
	public void setAd_img_desc(String ad_img_desc) {
		this.ad_img_desc = ad_img_desc;
	}
}
