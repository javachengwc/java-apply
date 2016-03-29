package com.solr.model;

/**
 * 商品图标 从广告位获取
 * 
 */
public class GoodsIcon {
	
	//商品id列表,逗号分隔
	private String goodsIds;
	
	//图标名称
	private String name;
	
	//图标图片
	private String img;
	
	//图片使用的期限条件
	private String dueExpr;
	
	//对应广告位广告id
	private Integer adId;

	public String getGoodsIds() {
		return goodsIds;
	}

	public void setGoodsIds(String goodsIds) {
		this.goodsIds = goodsIds;
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

	public String getDueExpr() {
		return dueExpr;
	}

	public void setDueExpr(String dueExpr) {
		this.dueExpr = dueExpr;
	}

	public Integer getAdId() {
		return adId;
	}

	public void setAdId(Integer adId) {
		this.adId = adId;
	}

}
