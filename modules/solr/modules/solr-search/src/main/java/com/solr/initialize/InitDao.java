package com.solr.initialize;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.solr.model.*;
import com.solr.util.DbUtil;
import com.solr.util.ServiceCfg;
import com.solr.util.SolrServerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 初始化频道页信息的DAO对象
 */
public class InitDao {

	/** 日志变量 */
	private static Logger log = LoggerFactory.getLogger(InitDao.class);

	static ResultSet rs = null;
	protected static Connection con = null;

	static PreparedStatement pst = null;

	private static ResourceBundle jdbcBundle = null;

	private static List<String> htmlParamsDbNamesList = null;

	private static Map<String, String> htmlParamsDbNamesMap = null;

	private static Map<String, String> keywordsMap = new HashMap<String, String>();
	private static Map<String, KeywordsInfo> keywordsInfoMap = null;
	private static Map<String, List<Map<String, String>>> commentMap = new HashMap<String, List<Map<String, String>>>();

	private static Map<String, String> cityMap = new HashMap<String, String>();
	private static Map<String, String> districtMap = new HashMap<String, String>();
	private static Map<String, String> provinceNameId = new HashMap<String, String>();
	private static Map<String, String> districtInfo = new HashMap<String, String>();
	private static List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
	private static List<String> deliveredInTimeArea = new ArrayList<String>();
	private static Map<String, Map<String, String>> cityProvinceArray = new HashMap<String, Map<String, String>>();

	// 初始化价格数据
	private static Map<String, List<Price>> priceIntervalMap = new HashMap<String, List<Price>>();

	private static List<String> citySortList = new CopyOnWriteArrayList<String>(new ArrayList<String>());

	// 品牌信息
	private static Map<String, String> brandInfoMap = new HashMap<String, String>();
	
	private static final Map<String,String> activityMap=new HashMap<String,String>();
	
	private static List<GoodsIcon> iconList =new ArrayList<GoodsIcon>();

	// 初始化排序权重参数
	private static List<ProductWeight> productWeightList = new ArrayList<ProductWeight>();

    static {
        // 初始化数据库连接信息
		ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);
		try {
			jdbcBundle = control.newBundle("jdbc", Locale.ROOT, "java.properties", InitDao.class.getClassLoader(), true);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


    /** 初始化连接 */
    public static final void initConnection() {
        con = getConnection();
    }

    /** 获取数据库连接 */
    private static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(jdbcBundle.getString("driver"));
            String url = jdbcBundle.getString("url");
            String user = jdbcBundle.getString("user");
            String password = jdbcBundle.getString("password");
            con = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    /** 初始化连接 */
    public static void initRuConnection() {
        con = getRuConnection();
    }

    /** 获取数据库连接 */
    protected static Connection getRuConnection() {
        Connection con = null;
        try {
            Class.forName(jdbcBundle.getString("driver"));
            String url = jdbcBundle.getString("ruurl");
            String user = jdbcBundle.getString("ruuser");
            String password = jdbcBundle.getString("rupassword");
            con = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    protected  static void close()
    {
        DbUtil.closeResource(con,pst,rs);
    }

    /**
	 * 初始化分组的id,name键值集合 Map<String,String>
	 */
	public static final void initGroupInfo() {

		/** 初始化连接 */
		initConnection();

		InitCatDao.initCatRelationParams();

		InitSeoDao.initSeoDao();
		InitSeoDao.initUsualSeo();
		InitSeoDao.initPublicSeo();
		InitSeoDao.initHotSearchKeywordsSeo();
		InitSeoDao.initBrandSeo();

		InitFacetDao.initSolrRelationParams();
        initKeywords();
        initCommentMap();

		setKeywordMap();
		setCityMap();
		setDistrictMap();
		setCityInfo();
		setDistrictInfo();
		fetchDeliveredInTimeArea();
		setProvinceMap();
		setCityList();
		initPriceData();
		initProductWeight();
		initCurrentActvity();
		initIconData();

        DbUtil.closeResource(con,pst,rs);

        log.error("主数据库数据读取完成");

		initCitySortList();

		log.error("初始化排序字段完成");
	}


    public static void initKeywords()
    {
        /** 查询热门搜索关键词 */
        keywordsInfoMap = getKeywordsInfoMap("select keyword ,des ,related_keyword, pinyin from ecs_searchkeywords where lang='cn' and keyword is not null and keyword !=''");

    }

	public static final void initCurrentActvity() {
		String sql = "select ad.desc ,ad.url,ad.src from ecs_ad_type t INNER JOIN ecs_cus_ad ad on ad.cat_id=t.id where ad_code='list_acti_filtrate' and ad.is_show=1";
		pst = getPst(con, sql);
		rs = getRs(pst);
		String url="";
		Long nowTime=new Date().getTime();
		activityMap.clear();
		DateFormat currentDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		try {
           while (rs.next()) {
        	   url=rs.getString("url");
        	   if(StringUtils.isEmpty(url)) {
        		   continue;
        	   }
        	   String[] activityTime=url.split("\\,");
        	   if(activityTime.length<2){
        		   continue;
        	   }
        	   Date beginDate=currentDateFormat.parse(activityTime[0]);
        	   Date endDate=currentDateFormat.parse(activityTime[1]);
        	   if(beginDate.getTime()<=nowTime && nowTime<endDate.getTime()){
        		   activityMap.put("activity_name",rs.getString("desc"));
        		   activityMap.put("activity_img",rs.getString("src"));
        		   break;
        	   }
        	   
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String,String> getCurrentActvity(){
		return activityMap;
	}

	/**
	 * 设置城市ID,name数据
	 */
	public static final int setCityMap() {
		String sql = "select region_id as id, region_name as name from ecs_region where region_type = 2 and usable = 1";
		cityMap.clear();
		cityMap = getMap(sql);
		return 0;
	}

	/**
	 * 初始化区id name数据
	 * 
	 * @return
	 */
	public static final int setDistrictMap() {
		String sql = "select region_id as id, region_name as name from ecs_region where region_type = 3 and usable = 1";
		districtMap.clear();
		districtMap = getMap(sql);
		return 0;
	}

	/**
	 * 读取后台设置的数据库权重值,并且判断权重字段是否在数据库中存在 ,后台约定所有排序自定以order_开始
	 */
	public static final int initProductWeight() {
		String sql = " select name,db_name dbName,weight_val value from ecs_java_search_weight  w "
				+ " inner join information_schema.columns c on w.db_name=c.column_name"
				+ " where table_schema='meilele_new' and table_name = 'java_city_channel' " + "  and w.is_use=1";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			productWeightList.clear();
			// 初始化静态的默认排序字段
			while (rs.next()) {
				ProductWeight productWeight = new ProductWeight();
				productWeight.setName(rs.getString("name"));
				productWeight.setField(rs.getString("dbName"));
				productWeight.setWeight(rs.getInt("value"));
				// 设置基准权重，保证所有的数据在一个范围之间
				productWeight.setBaseWeight(1000);
				if ("total_sold_yes_count".equals(productWeight.getField())) {
					productWeight.setFielfBf("div(total_sold_yes_count,1000)^" + productWeight.getWeight());
				} else if ("click_count".equals(productWeight.getField())) {
					productWeight.setFielfBf("div(click_count,100000)^" + productWeight.getWeight());
				} else if ("add_time".equals(productWeight.getField())) {
					productWeight.setFielfBf("recip(rord(add_time),1,100000,100000)^" + productWeight.getWeight());
				} else if ("effect_price".equals(productWeight.getField())) {
					productWeight.setFielfBf("div(effect_price,100000)^" + productWeight.getWeight());
				} else {
					// 其他属性统一的后台设置进行权重打分
					productWeight.setFielfBf("div(" + productWeight.getField() + "," + productWeight.getBaseWeight() + ")^" + productWeight.getWeight());
				}
				productWeightList.add(productWeight);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 设置城市ID,name数据
	 */
	public static final int initPriceData() {
		String sql = " select * from ecs_price_interval_setting where isvisible=1 ORDER BY cat_id,sort desc";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			priceIntervalMap.clear();
			while (rs.next()) {
				String catId = rs.getString("cat_id");
				List<Price> priceList = priceIntervalMap.get(catId);
				// 如果属性为空，就重新构造一个list
				if (priceList == null) {
					priceList = new ArrayList<Price>();
				}
				if (!priceIntervalMap.containsKey(catId)) {
					priceIntervalMap.put(catId, priceList);
				}
				Price price = new Price();
				price.setBegin(rs.getLong("low"));
				price.setEnd(rs.getLong("high"));
				price.setName(rs.getString("name"));
				priceList.add(price);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getBrandImageUrlByBrandId(String brandId) {
		return brandInfoMap.get(brandId);
	}

	public static List<Price> getPriceListByCatId(String catId) {
		return priceIntervalMap.get(catId);
	}

	private static void setProvinceMap() {
		String sql = "select region_id as id,region_name as name from ecs_region where region_type=1 order by region_id asc";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			provinceNameId.clear();
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				provinceNameId.put(name, id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 设置城市列表
	}

	private static final void setCityMap(List<Map<String, String>> cityList, String headName, String[] sonNames) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", headName);
		cityList.add(map);
		for (String key : sonNames) {
			map = new HashMap<String, String>();
			map.put("name", key);
			map.put("id", provinceNameId.get(key));
			cityList.add(map);
		}
		map = new HashMap<String, String>();
		map.put("name", "<br>");
		cityList.add(map);
	}

	private static final void setCityList() {
		cityList.clear();
		// 设置城市数据
		String headName = "<span class='bold'>华东</span>";
		String[] sonNames = new String[] { "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东" };
		setCityMap(cityList, headName, sonNames);

		headName = "<span class='bold'>华北</span>";
		sonNames = new String[] { "北京", "天津", "河北", "山西", "内蒙古" };
		setCityMap(cityList, headName, sonNames);

		headName = "<span class='bold'>华中</span>";
		sonNames = new String[] { "河南", "湖北", "湖南" };
		setCityMap(cityList, headName, sonNames);

		headName = "<span class='bold'>华南</span>";
		sonNames = new String[] { "广东", "广西", "海南" };
		setCityMap(cityList, headName, sonNames);

		headName = "<span class='bold'>东北</span>";
		sonNames = new String[] { "辽宁", "吉林", "黑龙江" };
		setCityMap(cityList, headName, sonNames);

		headName = "<span class='bold'>西南</span>";
		sonNames = new String[] { "重庆", "四川", "贵州", "云南", "西藏" };
		setCityMap(cityList, headName, sonNames);

		headName = "<span class='bold'>西北</span>";
		sonNames = new String[] { "陕西", "甘肃", "青海", "宁夏", "新疆" };
		setCityMap(cityList, headName, sonNames);

		headName = "<span class='bold'>其他</span>";
		sonNames = new String[] { "台湾", "香港", "澳门" };
		setCityMap(cityList, headName, sonNames);
	}

	private static final void setCityInfo() {
		String sql = "select b.region_id as city_id,b.region_name as city_name,a.region_id as province_id,a.region_name as province_name from ecs_region a "
				+ "join ecs_region b on a.region_id=b.parent_id where a.region_type=1 and b.region_type=2";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			cityProvinceArray.clear();
			while (rs.next()) {
				String city_id = rs.getString("city_id");
				String province_id = rs.getString("province_id");
				String province_name = rs.getString("province_name");
				Map<String, String> provices = cityProvinceArray.get(city_id);
				if (null == provices) {
					provices = new HashMap<String, String>();
					cityProvinceArray.put(city_id, provices);
				}
				provices.put("id", province_id);
				provices.put("name", province_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static final void setDistrictInfo() {
		String sql = "select c.region_id as dis_id,c.region_name as dis_name,b.region_id as city_id,b.region_name as city_name,a.region_id as province_id,a.region_name as province_name "
				+ "from ecs_region a "
				+ "join ecs_region b on a.region_id=b.parent_id "
				+ "join ecs_region c on b.region_id=c.parent_id "
				+ "where a.region_type = 1 and b.region_type = 2 and c.region_type = 3";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			districtInfo.clear();
			String disInfoString = "";
			while (rs.next()) {
				String dis_id = rs.getString("dis_id");
				disInfoString = rs.getString("dis_name") + "_" + rs.getString("city_id") + "_" + rs.getString("city_name") + "_" + rs.getString("province_id")
						+ "_" + rs.getString("province_name");
				districtInfo.put(dis_id, disInfoString);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void fetchDeliveredInTimeArea() {
		String sql = "SELECT " + "IFNULL(er.region_id,t1.region_id) AS dis_id " + "FROM ecs_limit_send_area t1 "
				+ "LEFT JOIN ecs_region er ON t1.region_id = er.parent_id "
				+ "LEFT JOIN ecs_limit_send_area t2 ON IFNULL(er.region_id,t1.region_id) = t2.region_id AND t2.type = 'out' "
				+ "WHERE t1.type = 'use' AND t2.id IS NULL";
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			deliveredInTimeArea.clear();
			while (rs.next()) {
				deliveredInTimeArea.add(rs.getString("dis_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置所有的关键字链表
	 */
	private static void setKeywordMap() {
		String sql = "select pinyin,keyword from ecs_searchkeywords where lang='cn' and keyword is not null and keyword !=''";
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			keywordsMap.clear();
			while (rs.next()) {
				String pinyin = rs.getString("pinyin");
				String keywords = rs.getString("keyword");
				if (keywords != null && !"".equals(keywords) && pinyin != null && !"".equals(pinyin)) {
					keywordsMap.put(pinyin, keywords);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** 单独更新用户自定义动态属性 */
	public static final void initUserDefinedPropertyOnly() {
		/** 初始化连接 */
		initConnection();
		// 用户自定义动态属性
		setAttrAndAttrVal();
		// 用户自定义动态属性
		// 初始化用户自定义属性参数，包括配置
		initUserDefinedConfiguration();
		/** 关闭流 */
        DbUtil.closeResource(con,pst,rs);
    }

	private static void setAttrAndAttrVal() {
		/** 初始化动态属性配置集合 */
		htmlParamsDbNamesMap = getMap(" select html_param_name id, attr_db_name name from mll_def_attr where type=1 ");
		htmlParamsDbNamesList = getList("select html_param_name id from mll_def_attr where type=1 order by sort_order asc");
	}

	/** 数据前先执行存储过程 */
	public static synchronized boolean dataImportProcedurePre() {
		/** 初始化连接 */
		initConnection();
		CallableStatement cst;
		try {
			cst = con.prepareCall("{call java_city_channel_pro()} ");
			cst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("call java_city_channel_pro()", e);
			return false;
		} finally {
            DbUtil.closeResource(con,pst,rs);
        }
		return true;
	}

	/** 更新评论榜 */
	private static void initCommentMap() {
		try {
			pst = getPst(
					con,
					"SELECT ec.pinyin,jcc.goods_name,gcj.* FROM goods_comment_java gcj join ecs_category ec on gcj.cat_id=ec.cat_id and ec.lang='cn' join java_city_channel jcc on gcj.goods_id=jcc.id where ec.is_show=1 group by gcj.goods_id");
			rs = getRs(pst);
			commentMap.clear();
			while (rs.next()) {
				Map<String, String> topCom = new HashMap<String, String>();
				String goods_id = rs.getString("goods_id");
				String goods_thumb_1 = rs.getString("goods_thumb_1");
				if (StringUtils.isBlank(goods_thumb_1)) {
					continue;
				}
				String cat_id = rs.getString("cat_id");
				String content = rs.getString("content");
				if (StringUtils.isNotBlank(content)) {
					content = content.replaceAll("<[^>]+>", "");
				}
				if (content.length() > 12) {
					content = content.substring(0, 12) + "...";
				}
				topCom.put("cat_id", cat_id);
				topCom.put("goods_url", "/category-" + rs.getString("pinyin") + "/goods-" + goods_id + ".html");
				topCom.put("goods_id", goods_id);
				topCom.put("goods_name", rs.getString("goods_name"));
				topCom.put("content", content);
				String url = "/goods_comments-" + goods_id + "-1.html";
				topCom.put("url", url);
				topCom.put("goods_thumb_1", goods_thumb_1);

				// 添加产品价格和评论人名字信息
				String shop_price = rs.getString("shop_price");
				if (null != shop_price && !"".equals(shop_price)) {
					shop_price = shop_price.split("\\.")[0];
				}
				topCom.put("shop_price", shop_price);
				String user_name = rs.getString("user_name");
				user_name = dealMailAndNum(user_name);
				topCom.put("user_name", user_name);

				List<Map<String, String>> commentList = commentMap.get(cat_id);
				if (null == commentList) {
					commentList = new ArrayList<Map<String, String>>();
				}
				commentList.add(topCom);
				commentMap.put(cat_id, commentList);
			}

			// 插入一条键为null的记录，为所有评论的最新评论
			pst = getPst(
					con,
					"SELECT ec.pinyin,jcc.goods_name,gcj.* FROM goods_comment_java gcj join ecs_category ec on gcj.cat_id=ec.cat_id and ec.lang='cn' join java_city_channel jcc on gcj.goods_id=jcc.id where ec.is_show=1 group by gcj.goods_id LIMIT 5");
			rs = getRs(pst);
			ArrayList<Map<String, String>> commentAllList = new ArrayList<Map<String, String>>();
			while (rs.next()) {
				Map<String, String> topCom = new HashMap<String, String>();
				String goods_id = rs.getString("goods_id");
				String goods_thumb_1 = rs.getString("goods_thumb_1");
				if (StringUtils.isBlank(goods_thumb_1)) {
					continue;
				}
				String content = rs.getString("content");
				String cat_id = rs.getString("cat_id");
				if (content.length() > 12) {
					content = content.substring(0, 12) + "...";
				}
				topCom.put("cat_id", cat_id);
				topCom.put("goods_url", "/category-" + rs.getString("pinyin") + "/goods-" + goods_id + ".html");
				topCom.put("goods_id", goods_id);
				topCom.put("goods_name", rs.getString("goods_name"));
				topCom.put("content", content);
				String url = "/goods_comments-" + goods_id + "-1.html";
				topCom.put("url", url);
				topCom.put("goods_thumb_1", goods_thumb_1);

				// 添加产品价格和评论人名字信息
				String shop_price = rs.getString("shop_price");
				if (null != shop_price && !"".equals(shop_price)) {
					shop_price = shop_price.split("\\.")[0];
				}
				topCom.put("shop_price", shop_price);
				String user_name = rs.getString("user_name");
				user_name = dealMailAndNum(user_name);
				topCom.put("user_name", user_name);

				commentAllList.add(topCom);
			}
			commentMap.put("all", commentAllList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对手机号码和邮箱名称的屏蔽
	 */
	public static String dealMailAndNum(String user_name) {
		// --增加对user_name格式的过滤---
		String regx = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";// 手机号码
		String regy = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";// 邮箱
		if (StringUtils.isEmpty(user_name)) {
			return "";
		}
		if (user_name.length() < 4) {
			return user_name;
		}

		if (user_name.matches(regx)) {
			String s1 = user_name.substring(0, 3);
			String s2 = user_name.substring(user_name.length() - 4, user_name.length());
			String d = "****";
			user_name = s1 + d + s2;
		}
		if (user_name.matches(regy)) {
			if (user_name.indexOf("@") < 5) {
				user_name = user_name.substring(0, 1) + getMaskCode(user_name.indexOf("@") - 1) + user_name.substring(user_name.indexOf("@"));
			} else {
				String s1 = user_name.substring(0, user_name.indexOf("@") - 4);
				String s2 = user_name.substring(user_name.indexOf("@"));
				String d = "****";
				user_name = s1 + d + s2;
			}
		}
		return user_name;
	}

	private static String getMaskCode(int index) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < index; i++) {
			sb.append("*");
		}
		return sb.toString();
	}

	protected static Map<String, String> getMap(String sql) {
		/** 查询品牌的ID，NAME对应的Map集合 */
		pst = getPst(con, sql);
		rs = getRs(pst);
		return getMap(rs);
	}

	private static List<String> getList(String sql) {
		pst = getPst(con, sql);
		rs = getRs(pst);
		return getList(rs);
	}

	static String getStrResult(String sql) {
		pst = getPst(con, sql);
		rs = getRs(pst);
		return getStrResult(rs);
	}

	private static Map<String, List<CategoryAdImg>> getCatAdImg(String sql) {
		pst = getPst(con, sql);
		rs = getRs(pst);
		return getCatAdImg(rs);
	}

	private static Map<String, KeywordsInfo> getKeywordsInfoMap(String sql) {
		pst = getPst(con, sql);
		rs = getRs(pst);
		return getKeywordsInfoMap(rs);
	}

	/**
	 * 查询/keywords/对应的信息 Map<String,KeywordsInfo>
	 */
	private static Map<String, KeywordsInfo> getKeywordsInfoMap(ResultSet rs) {
		Map<String, KeywordsInfo> map = new HashMap<String, KeywordsInfo>();
		try {
			while (rs.next()) {
				KeywordsInfo ki = new KeywordsInfo();
				ki.setDes(rs.getString("des"));
				ki.setKeyword(rs.getString("keyword"));
				ki.setPinyin(rs.getString("pinyin"));
				ki.setRelated_keyword(rs.getString("related_keyword"));
				map.put(rs.getString("pinyin"), ki);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取分类广告图 String
	 */
	private static Map<String, List<CategoryAdImg>> getCatAdImg(ResultSet rs) {
		Map<String, List<CategoryAdImg>> map = new HashMap<String, List<CategoryAdImg>>();
		try {
			while (rs.next()) {
				CategoryAdImg cai = new CategoryAdImg();
				if (rs.getString("img") == null || "".equals(rs.getString("img"))) {
					continue;
				}
				String name = rs.getString("name");
				cai.setName(name);
				String cat_id = "";
				if (name.matches("[0-9]+([,:;\"\'!@#$%^&*().a-zA-Z0-9\u4E00-\u9FA5\uFE30-\uFFA0\u3001-\u3003])*")) {
					cat_id = name.replaceAll("([,:;\"\'!@#$%^&*().\u4E00-\u9FA5\uFE30-\uFFA0\u3001-\u3003])*", "");
				}
				if (StringUtils.isBlank(cat_id)) {
					continue;
				}// 如果没有取到cat_id,就不保存数据
				cai.setImg(rs.getString("img"));
				cai.setUrl(rs.getString("url").replace("http://www.meilele.com", ""));
				cai.setAd_img_desc(rs.getString("ad_img_desc"));
				cai.setGoods_id(rs.getString("goods_id"));
				cai.setAd_img_id(rs.getString("ad_img_id"));
				List<CategoryAdImg> list = map.get(cat_id);
				if (list == null) {
					list = new ArrayList<CategoryAdImg>();
				}
				list.add(cai);
				map.put(cat_id, list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取预编译对象 PreparedStatement
	 */
	static PreparedStatement getPst(Connection con, String sql) {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pst;
	}

	/**
	 * 获取结果集对象 ResultSet
	 */
	static ResultSet getRs(PreparedStatement pst) {
		ResultSet rs = null;
		try {
			rs = pst.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 将结果集遍历装入map集合中 Map<String,String>
	 */
	static Map<String, String> getMap(ResultSet rs) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				map.put(id, name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 将结果集遍历装入map集合中 Map<String,String>
	 */
	static List<String> getList(ResultSet rs) {
		List<String> list = new ArrayList<String>();
		try {
			while (rs.next()) {
				String id = rs.getString("id");
				list.add(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 获取结果集中的单个值 */
	static String getStrResult(ResultSet rs) {
		String value = "";
		try {
			while (rs.next()) {
				value = rs.getString("value");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static final KeywordsInfo getKeywordsInfoByName(String name) {
		return keywordsInfoMap.get(name);
	}

	public static final String getDbNameByHtmlParam(String htmlParam) {
		return htmlParamsDbNamesMap.get(htmlParam);
	}

	public static final List<String> getHtmlParamsDbNamesList() {
		return htmlParamsDbNamesList;
	}

	public static final String getKeywordsByPinyin(String pinyin) {
		return keywordsMap.get(pinyin);
	}

	public static final String getCityNameById(String city_id) {
		return cityMap.get(city_id);
	}

	public static final String getDistrictNameById(String dis_id) {
		return districtMap.get(dis_id);
	}

	public static final Map<String, String> getProvinceListByCityId(String city_id) {
		return cityProvinceArray.get(city_id);
	}

	public static final List<String> getDeliveredInTimeArea() {
		return deliveredInTimeArea;
	}

	/** 页面参数对应的solr参数的键值, 例:b对应brand_id */
	private static Map<String, String> solrKeyMap = new HashMap<String, String>();
	/** 用户自定义数据键值，用于自定义属性增加删除修改 **/
	private static Map<String, String> dynamicSolrKeyMap = new HashMap<String, String>();

	private static List<String> userDefinedList = new ArrayList<String>();
	/** 动态添加的属性值 **/
	private static List<String> dynamicUserDefinedList = new ArrayList<String>();

	/** params名字和各分类的IdNameMap的对应数字参数的映射 */
	private static Map<String, Integer> paramsNameSolrMap = new HashMap<String, Integer>();
	/** 用户自定义数据，用于对应数字参数映射的增加删除修改 **/
	private static Map<String, Integer> dynamicParamsNameSolrMap = new HashMap<String, Integer>();
	public final static int PROPERTYIDVALUEMAP = 7;

	public static void initUserDefinedConfiguration() {
		List<String> configureList = InitDao.getHtmlParamsDbNamesList();
		/** 先移除之前添加的分类属性 */
		userDefinedList.removeAll(dynamicUserDefinedList);
		for (String param : dynamicUserDefinedList) {
			paramsNameSolrMap.remove(param);
			solrKeyMap.remove(param);
		}

		/** 清空用户自定义分类属性集合 */
		dynamicUserDefinedList.clear();
		dynamicParamsNameSolrMap.clear();
		dynamicSolrKeyMap.clear();
		for (String htmlParam : configureList) {

            dynamicUserDefinedList.add(htmlParam);

			dynamicSolrKeyMap.put(htmlParam, getDbNameByHtmlParam(htmlParam));

			dynamicParamsNameSolrMap.put(htmlParam, InitDao.PROPERTYIDVALUEMAP);

		}

		userDefinedList.addAll(dynamicUserDefinedList);
		paramsNameSolrMap.putAll(dynamicParamsNameSolrMap);
		solrKeyMap.putAll(dynamicSolrKeyMap);

	}

	public static final List<Map<String, String>> getCityList() {
		return cityList;
	}

	public static List<ProductWeight> getProductWeightList() {
		return productWeightList;
	}

	public static String getDistrictInfoById(String districtId) {
		return districtInfo.get(districtId);
	}
	
	public static final void initIconData() {
		String sql = "select ad.id,ad.desc,ad.url,ad.src,GROUP_CONCAT(r.regular_name) as goods_ids "+
						"from ecs_ad_type t "+ 
						"join ecs_cus_ad ad on ad.cat_id=t.id "+ 
						"join ecs_ad_regular r on r.ad_id=ad.id "+
						"where t.ad_code='goods_list_tbad' and ad.is_show=1 "+
						"group by ad.id";
		pst = getPst(con, sql);
		rs = getRs(pst);
		String url="";
		Long nowTime=new Date().getTime();
		List<GoodsIcon> tmpIconList= new ArrayList<GoodsIcon>();
		DateFormat currentDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		try {
           while (rs.next()) {
        	   
        	   boolean add=true;
        	   url=rs.getString("url");
        	   if(!StringUtils.isEmpty(url))
        	   {
	        	   String[] dueTime=url.split("\\,");
	        	   if(dueTime.length>=2)
	        	   {
		        	   Date beginDate=currentDateFormat.parse(dueTime[0]);
		        	   Date endDate=currentDateFormat.parse(dueTime[1]);
		        	   if(beginDate.getTime()>nowTime || endDate.getTime()<=nowTime){
		        		   add=false;
		        	   }
	        	   }
        	   }
        	   if(add)
        	   {
        		   GoodsIcon goodsIcon = new GoodsIcon();
        		   goodsIcon.setName(rs.getString("desc"));
        		   goodsIcon.setImg(rs.getString("src"));
        		   goodsIcon.setDueExpr(rs.getString("url"));
        		   goodsIcon.setGoodsIds(rs.getString("goods_ids"));
        		   goodsIcon.setAdId(rs.getInt("id"));
        		   
        		   tmpIconList.add(goodsIcon);
        	   }
        	   
			}
            
           iconList= tmpIconList;
            
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据商品号获取图标
	 */
	public static String getIconImgByGoodsId(String id)
	{
		if(StringUtils.isBlank(id) || iconList.size()<=0)
		{
			return "";
		}
		for(GoodsIcon icon:iconList)
		{
			if(StringUtils.isBlank(icon.getGoodsIds()))
			{
				continue;
			}
			Pattern pattern = Pattern.compile("\\D*"+id+"\\D*");
			Matcher ma =pattern.matcher(icon.getGoodsIds());
			if(ma.find())
			{
				return icon.getImg();
			}
		}
		return "";
	}

    public static final void initCitySortList() {

        SolrServer server = SolrServerFactory.getSolrServerInstance(ServiceCfg.getServiceParameterByName("default").get("solrRequestUrl"));
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.set("start", "0");
        query.set("rows", "1");
        QueryResponse queryReponse;
        try {
            queryReponse = server.query(query);
            SolrDocumentList documents = queryReponse.getResults();
            citySortList.clear();
            for (SolrDocument document : documents) {
                citySortList.addAll(document.getFieldNames());
            }

        } catch (SolrServerException e) {
            e.printStackTrace();
        }

    }

    public static boolean isContainsSortField(String filed) {
        return citySortList.contains(filed);
    }

}