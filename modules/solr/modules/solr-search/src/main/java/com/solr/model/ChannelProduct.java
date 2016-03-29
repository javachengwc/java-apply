package com.solr.model;

import com.solr.initialize.InitDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.beans.Field;

public class ChannelProduct {
	@Field
	private int		is_postfree;
	@Field
	private float	discount_price;
	@Field
	private int		discount_type;
	@Field("goods_url")
	private String	goods_uri;
	@Field
	private String	goods_thumb;
	@Field
	private String	goods_thumb_1;
	@Field
	private String	goods_thumb_2;
	@Field
	private String	brand_name;
	@Field
	private String	new_goods_name;
	@Field("effect_price")
	private float	show_price;			//effectPrice
	@Field
	private float	market_price;
	@Field
	private String	id;
	@Field
	private long	total_sold_yes_count;
	@Field
	private long	comment_good_count;
	@Field
	private long	click_count;
	@Field
	private String	goods_comment;
	@Field
	private String	user_name;
	@Field
	private String	activity_name;
	@Field
	private boolean is_force_show;
	@Field
	private String  desc;
	@Field
	private String  ad_code;
	@Field
	private String	price_name;
	@Field
	private String	allocate_city;
	@Field
	private String	spot_city;
	@Field
	private String  style_name;
	@Field
	private int     show_type;
	@Field
	private String  red_title;
	@Field
	private String  goods_name;
	@Field
	private int     shop_id;
	private int	    is_real_time;
	@Field
	private String  goods_sn;
	private String  advertisementTag;
	@Field
	private int     is_install;
	@Field
	private float  shop_price;
	
	private int     isDeliveredInTime;
	@Field
	private int     is_in_quanwugou;
	
	@Field
	private boolean is_activity;
	
	@Field
    private int     goods_attribute;
	
	//商品图标
	private String icon_img="";
	
	public float getDiscount_price() {
		return discount_price;
	}

	public void setDiscount_price(float discount_price) {
		this.discount_price = discount_price;
	}

	public int getDiscount_type() {
		return discount_type;
	}

	public void setDiscount_type(int discount_type) {
		this.discount_type = discount_type;
	}

	public String getGoods_uri() {
		return goods_uri;
	}

	public void setGoods_uri(String goods_uri) {
		this.goods_uri = goods_uri;
	}

	public final String getGoods_thumb() {
		return goods_thumb;
	}

	public final void setGoods_thumb(String goods_thumb) {
		this.goods_thumb = goods_thumb;
	}

	public String getGoods_thumb_1() {
		return goods_thumb_1;
	}

	public void setGoods_thumb_1(String goods_thumb_1) {
		this.goods_thumb_1 = goods_thumb_1;
	}

	public String getGoods_thumb_2() {
		return goods_thumb_2;
	}

	public void setGoods_thumb_2(String goods_thumb_2) {
		this.goods_thumb_2 = goods_thumb_2;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getNew_goods_name() {
		return new_goods_name;
	}

	public void setNew_goods_name(String new_goods_name) {
		this.new_goods_name = new_goods_name;
	}

	public float getShow_price() {
		return show_price;
	}

	public void setShow_price(float show_price) {
		this.show_price = show_price;
	}

	public float getMarket_price() {
		return market_price;
	}

	public void setMarket_price(float market_price) {
		this.market_price = market_price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTotal_sold_yes_count() {
		return total_sold_yes_count;
	}

	public void setTotal_sold_yes_count(long total_sold_yes_count) {
		this.total_sold_yes_count = total_sold_yes_count;
	}

	public long getClick_count() {
		return click_count;
	}

	public void setClick_count(long click_count) {
		this.click_count = click_count;
	}

	public String getGoods_comment() {
		return goods_comment;
	}

	public void setGoods_comment(String goods_comment) {
		this.goods_comment = goods_comment;
	}

	public String getUser_name() {
		if (StringUtils.isNotBlank(user_name)) {
			user_name = InitDao.dealMailAndNum(user_name);
		}
		return user_name;
	}

	public void setUser_name(String user_name) {
		if (StringUtils.isNotBlank(user_name)) {
			user_name = InitDao.dealMailAndNum(user_name);
		}
		this.user_name = user_name;
	}

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public String getPrice_name() {
		return price_name;
	}

	public void setPrice_name(String price_name) {
		this.price_name = price_name;
	}

	public String getAllocate_city() {
		return allocate_city;
	}

	public void setAllocate_city(String allocate_city) {
		this.allocate_city = allocate_city;
	}

	public String getSpot_city() {
		return spot_city;
	}

	public void setSpot_city(String spot_city) {
		this.spot_city = spot_city;
	}

	public int getIs_real_time() {
		return is_real_time;
	}

	public void setIs_real_time(int is_real_time) {
		this.is_real_time = is_real_time;
	}

	public long getComment_good_count() {
		return comment_good_count;
	}

	public void setComment_good_count(long comment_good_count) {
		this.comment_good_count = comment_good_count;
	}

	public final int getIs_postfree() {
		return is_postfree;
	}

	public final void setIs_postfree(int is_postfree) {
		this.is_postfree = is_postfree;
	}

	public String getStyle_name() {
		return style_name;
	}

	public void setStyle_name(String style_name) {
		this.style_name = style_name;
	}

	public String getRed_title() {
		return red_title;
	}

	public void setRed_title(String red_title) {
		this.red_title = red_title;
	}

	public int getShow_type() {
		return show_type;
	}

	public void setShow_type(int show_type) {
		this.show_type = show_type;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getAdvertisementTag() {
		return advertisementTag;
	}

	public void setAdvertisementTag(String advertisementTag) {
		this.advertisementTag = advertisementTag;
	}

	public String getGoods_sn() {
		return goods_sn;
	}

	public void setGoods_sn(String goods_sn) {
		this.goods_sn = goods_sn;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean getIs_force_show() {
		return is_force_show;
	}

	public void setIs_force_show(boolean is_force_show) {
		this.is_force_show = is_force_show;
	}

	public String getAd_code() {
		return ad_code;
	}

	public void setAd_code(String ad_code) {
		this.ad_code = ad_code;
	}

	public int getShop_id() {
		return shop_id;
	}

	public void setShop_id(int shop_id) {
		this.shop_id = shop_id;
	}

	public int getIs_install() {
		return is_install;
	}

	public void setIs_install(int is_install) {
		this.is_install = is_install;
	}

	public int getIsDeliveredInTime() {
		return isDeliveredInTime;
	}

	public void setIsDeliveredInTime(int isDeliveredInTime) {
		this.isDeliveredInTime = isDeliveredInTime;
	}



	public int getIs_in_quanwugou() {
		return is_in_quanwugou;
	}

	public void setIs_in_quanwugou(int is_in_quanwugou) {
		this.is_in_quanwugou = is_in_quanwugou;
	}

	public float getShop_price() {

		return shop_price;
	}

	public void setShop_price(float shop_price) {
		this.shop_price = shop_price;
	}

	public boolean isIs_activity() {
		return is_activity;
	}

	public void setIs_activity(boolean is_activity) {
		this.is_activity = is_activity;
	}

	public String getIcon_img() {
		return icon_img;
	}

	public void setIcon_img(String icon_img) {
		this.icon_img = icon_img;
	}

    public int getGoods_attribute() {
        return goods_attribute;
    }

    public void setGoods_attribute(int goods_attribute) {
        this.goods_attribute = goods_attribute;
    }
}
