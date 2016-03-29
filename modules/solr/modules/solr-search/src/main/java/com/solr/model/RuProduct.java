package com.solr.model;

import org.apache.solr.client.solrj.beans.Field;

public class RuProduct {
	@Field
	private String id;
	@Field
	private String goods_sn;
	@Field
	private String goods_name;
	@Field
	private String goods_url;
	@Field
	private String goods_thumb;
	@Field
	private String goods_thumb_1;
	@Field
	private float effect_price;
	@Field
	private float market_price;

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getGoods_sn() {
		return goods_sn;
	}

	public final void setGoods_sn(String goods_sn) {
		this.goods_sn = goods_sn;
	}

	public final String getGoods_name() {
		return goods_name;
	}

	public final void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public final String getGoods_url() {
		return goods_url;
	}

	public final void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}

	public final String getGoods_thumb() {
		return goods_thumb;
	}

	public final void setGoods_thumb(String goods_thumb) {
		this.goods_thumb = goods_thumb;
	}

	public final String getGoods_thumb_1() {
		return goods_thumb_1;
	}

	public final void setGoods_thumb_1(String goods_thumb_1) {
		this.goods_thumb_1 = goods_thumb_1;
	}

	public final float getEffect_price() {
		return effect_price;
	}

	public final void setEffect_price(float effect_price) {
		this.effect_price = effect_price;
	}

	public final float getMarket_price() {
		return market_price;
	}

	public final void setMarket_price(float market_price) {
		this.market_price = market_price;
	}
}
