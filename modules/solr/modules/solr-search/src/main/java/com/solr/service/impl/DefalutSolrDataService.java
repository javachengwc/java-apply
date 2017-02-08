package com.solr.service.impl;

import com.solr.model.Activity;
import com.solr.model.Price;
import com.solr.model.context.MyContext;
import com.solr.initialize.*;
import com.solr.util.ServiceCfg;
import com.solr.util.SolrServerFactory;
import com.solr.model.*;
import com.solr.service.SolrDataService;
import com.solr.util.SysConfig;
import com.util.base.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefalutSolrDataService implements SolrDataService {

    public static final String CITY = "city";
    public static final String DF_CITY = "0";//默认的全国的城市ID
    public static final String RT  = "rt";

	protected UrlService urlService;
	private Map<String, String> solrUrlParameter;
	private List<String> baseFacetField = null;
	private List<String> selectList = new ArrayList<String>();
	private static List<Activity> deliverys =new ArrayList<Activity>();

    static{
		Activity allDelivery=new Activity();
		allDelivery.setName("全部");
		allDelivery.setValue("0");
		deliverys.add(allDelivery);
		Activity selfDelivery=new Activity();
		selfDelivery.setName("商城配送");
		selfDelivery.setValue("1");
		deliverys.add(selfDelivery);
		Activity thirdDelivery=new Activity();
		thirdDelivery.setName("第三方配送");
		thirdDelivery.setValue("2");
		deliverys.add(thirdDelivery);
	}
	public DefalutSolrDataService(UrlService urlService) {
		this.urlService = urlService;
		solrUrlParameter = this.urlService.getSolrQueryParameter();
		baseFacetField = new ArrayList<String>();
		baseFacetField.add("mdname");
		baseFacetField.add("sdname");
		baseFacetField.add("bdname");
		baseFacetField.add("cdname");
	}

	/**
	 * 处理排序数据
	 */
	public void dealSorlSort(Map<String, String> solrSortParameter, MyContext context) {
		// 处理排序数据，如果当前是倒序，url处理为顺序。反之相反
		String order_total_sold_yes_count_url = urlService.buildSortUrl("sort", "total_sold_yes_count", "desc", solrSortParameter);
		String order_effect_price_url = urlService.buildSortUrl("sort", "effect_price", "asc", solrSortParameter);
		String order_click_count_url = urlService.buildSortUrl("sort", "click_count", "desc", solrSortParameter);
		String order_add_time_url = urlService.buildSortUrl("sort", "add_time", "asc", solrSortParameter);
		context.set("order_total_sold_yes_count_url", order_total_sold_yes_count_url);
		context.set("order_effect_price_url", order_effect_price_url);
		context.set("order_click_count_url", order_click_count_url);
		context.set("order_add_time_url", order_add_time_url);
		context.set("order", solrSortParameter.get("order"));
		context.set("sort", solrSortParameter.get("sort"));
		// context.set("current_order",solrSortParameter.get("current_order"));
		// context.set("current_sort",solrSortParameter.get("current_sort"));
	}

	/**
	 * 处理价格数据
	 */
	public Map<String, Object> dealPriceData(String catId, MyContext context, Map<String, Object> solrFactParameter) {

		// 从数据库里面读取价格数据，然后根据价格数据构建url
		List<Price> priceList = null;
		if (StringUtils.isNotEmpty(catId)) {
			priceList = InitDao.getPriceListByCatId(catId);
		}
		// 如果价格为空,就用默认的价格属性。后台设置的价格为空，包括没有catId和有catId时后台没有设置
		if (priceList == null) {
			priceList = ServiceCfg.getPriceListByName("default");
		}
		Map<String,Integer> priceQueryResult=new HashMap<String,Integer>();
		if(context.getValueMap().get("priceQueryResult")!=null){
		    priceQueryResult=(Map<String, Integer>) context.getValueMap().get("priceQueryResult");
		}
		Map<String, Object> priceMap = new HashMap<String, Object>();
		List<Map<String, String>> priceResult = new ArrayList<Map<String, String>>();
		Map<String, String> priceSelectMap = this.urlService.getPriceMap();
		boolean hasMatchSelectedPrice = false;
		for (Price price : priceList) {
			Map<String, String> priceDatailMap = new HashMap<String, String>();
			priceDatailMap.put("name", price.getName());
			String minPriceUrl = urlService.buildUrl("min", String.valueOf(price.getBegin()));
			String maxPriceUlr = urlService.buildUrl(minPriceUrl, "max", String.valueOf(price.getEnd()));
			priceDatailMap.put("url", maxPriceUlr);
			String priceKey=String.format("effect_price:[%s TO %s]",String.valueOf(price.getBegin()),price.getEnd()==0?"*":String.valueOf(price.getEnd()));
			if(priceQueryResult.get(priceKey)==null || priceQueryResult.get(priceKey).intValue()<=0 ){
			    continue;
			}
			priceDatailMap.put("count",String.valueOf(priceQueryResult.get(priceKey).intValue()));
			priceResult.add(priceDatailMap);
			// 设置价格选中字段
			if (String.valueOf(price.getBegin()).equals(priceSelectMap.get("min")) && String.valueOf(price.getEnd()).equals(priceSelectMap.get("max"))) {
				context.set("not_select_price", "no");
				context.set("select_price_name", price.getName());
				context.set("select_price", price.getName());
				 priceMap.put("select_core_name","价格: "+ price.getName());
				 priceMap.put("select_core_id",price.getName());
				// 有价格选中的时候,也设置is_select_condition为yes
				context.set("is_select_condition", "yes");
				// 设置选中的价格清除url
				String selectMinPriceUrl = urlService.buildUrl("min", String.valueOf(0));
				String selectMaxPriceUlr = urlService.buildUrl(selectMinPriceUrl, "max", String.valueOf(0));
				context.set("all_price_url", selectMaxPriceUlr);
				priceMap.put("all_core_cat_url",selectMaxPriceUlr);
				// 设置选中的价格到选中list
				selectList.add(price.getName());
				context.set("is_midbar_fold", 1);
				hasMatchSelectedPrice = true;
			}
		}
		// 如果没有匹配搭配设置的价格区间,就是手工输入的价格区间
		if (!hasMatchSelectedPrice) {
			if (priceSelectMap != null && priceSelectMap.size() == 2) {
			    hasMatchSelectedPrice = true;
				String min = priceSelectMap.get("min");
				String max = priceSelectMap.get("max");
				// 价格不能同时为0
				if (!("0".endsWith(min) && "0".equals(max))) {
					context.set("not_select_price", "no");
					String priceName = priceSelectMap.get("min") + "-" + priceSelectMap.get("max");
					context.set("select_price_name", priceName);
					context.set("select_price", priceName);
					 priceMap.put("select_core_name",priceName);
					 priceMap.put("select_core_id",priceName);
					 priceMap.put("isDiyPrice","1");
					// 有价格选中的时候,也设置is_select_condition为yes
					context.set("is_select_condition", "yes");
					// 设置选中的价格清除url
					String selectMinPriceUrl = urlService.buildUrl("min", String.valueOf(0));
					String selectMaxPriceUlr = urlService.buildUrl(selectMinPriceUrl, "max", String.valueOf(0));
					context.set("all_price_url", selectMaxPriceUlr);
					priceMap.put("all_core_cat_url",selectMaxPriceUlr);
					// 设置选中的价格到选中list
					selectList.add(priceName);
					context.set("is_midbar_fold", 1);
					context.set("selectedPriceMin", priceSelectMap.get("min"));
					context.set("selectedPriceMax", priceSelectMap.get("max"));
				}
			}
		}

		// 构造手动输入价格的区间base url
		String baseMinPriceUrl = urlService.buildUrl("min", String.valueOf(0));
		String basemaxPriceUlr = urlService.buildUrl(baseMinPriceUrl, "max", String.valueOf(0));
		context.set("basePriceUrl", basemaxPriceUlr);
		int attr_sort = 0;
		// 处理价格的排序
		String facetId = InitFacetDao.getCatAttrIdByDbName("price");
		if (StringUtils.isNotEmpty(catId) && StringUtils.isNotEmpty(facetId)) {
			String sortKey = catId + "-" + facetId;
			attr_sort = InitFacetDao.getCatSortValue(sortKey);
		}
		if(hasMatchSelectedPrice){
		    priceMap.put("priceSelected", "1");
		}else{
		    priceMap.put("priceSelected", "0");
		}
		priceMap.put("name", "价格");
		priceMap.put("sort", attr_sort);
		priceMap.put("core_list", priceResult);
		priceList = null;
		priceSelectMap = null;
		return priceMap;
	}

	public void dealActivityData(MyContext context) {
		List<Activity> activitys = ServiceCfg.getActivitysByName("default");
		List<Map<String, String>> actArray = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		for (Activity activity : activitys) {
			map = new HashMap<String, String>();
			map.put("name", activity.getValue());
			map.put("url", urlService.buildActivityUrl(activity.getName()));
			actArray.add(map);
			// 设置活动的选中字段，如果活动类型中已经有活动地址了，就取消原来的活动地址
			if (solrUrlParameter.get("activity_search_name") != null && solrUrlParameter.get("activity_search_name").indexOf(activity.getName()) > 0) {
				context.set("select_act", map);
				// 设置选中的价格到选中list
				selectList.add(activity.getValue());
			}

		}
		activitys = null;
		context.set("actArray", actArray);
	   //加入国庆，周年庆等大活动信息
		Map<String,String> activity=new HashMap<String,String>();
		activity.putAll(InitDao.getCurrentActvity());
		 if(StringUtils.isNotEmpty(solrUrlParameter.get("is_activity"))){
			 activity.put("url", urlService.buildActivitysUrl("0"));
			 activity.put("is_selected","1");
		 }else{
			 activity.put("url", urlService.buildActivitysUrl("1"));
			 activity.put("is_selected","0");
		 }
		 context.set("new_activity", activity);
	}

	public void dealDeliveredInTimeData(Map<String, String> rtParameter, MyContext context) {
		String districtId = rtParameter.get("city");
		if (StringUtils.isBlank(districtId)) {
			return;
		}
		boolean isDistrictInDeliveredInTimeArea = InitDao.getDeliveredInTimeArea().contains(districtId);
		if (!isDistrictInDeliveredInTimeArea) {
			return;
		}
		Map<String, String> deliveredInTimeDataMap = new LinkedHashMap<String, String>(8);
		context.set("delivered_in_time", deliveredInTimeDataMap);
		String deliveredInTimeUrl = urlService.buildUrl("lt", "0");
		if (!deliveredInTimeUrl.contains("-lt0")) {
			StringBuilder buf = new StringBuilder(deliveredInTimeUrl);
			buf.insert(deliveredInTimeUrl.indexOf(".html"), "-lt0");
			deliveredInTimeUrl = buf.toString();
		}
		deliveredInTimeUrl = deliveredInTimeUrl.replace("rt1", "rt0");
		deliveredInTimeDataMap.put("0", deliveredInTimeUrl.replace("-lt0", ""));
		deliveredInTimeDataMap.put("1", deliveredInTimeUrl.replace("-lt0", "-lt1"));
		deliveredInTimeDataMap.put("2", deliveredInTimeUrl.replace("-lt0", "-lt2"));
		deliveredInTimeDataMap.put("3", deliveredInTimeUrl.replace("-lt0", "-lt3"));
		String deliveredInTimeFlag = StringUtils.isNotBlank(rtParameter.get("deliveredInTimeFlag")) ? rtParameter.get("deliveredInTimeFlag") : "0";
		context.set("deliveredInTimeFlag", deliveredInTimeFlag);
	}
	
	public void dealProvinceData(MyContext context) {
		context.set("province_list", InitDao.getCityList());
		// 处理选中的省份和城市信息
		String dis_name      = "";
		String city_id       = "";
		String city_name     = "";
		String province_id   = "";
		String province_name = "";
		String dis_id = this.urlService.getRtParameter().get(CITY);
		if (StringUtils.isNotBlank(dis_id) && !DF_CITY.equals(dis_id)) {
			dis_name = InitDao.getDistrictNameById(dis_id);
			// dis_id为三级区id
			if (StringUtils.isNotBlank(dis_name)) {
				String districtInfoString = InitDao.getDistrictInfoById(dis_id);
				String[] districtInfoArray = districtInfoString.split("_");
				dis_name      = districtInfoArray[0];
				city_id       = districtInfoArray[1];
				city_name     = districtInfoArray[2];
				province_id   = districtInfoArray[3];
				province_name = districtInfoArray[4];
				context.set("dis_id", dis_id);
				context.set("dis_name", dis_name);
			// dis_id为二级城市id
			} else {
				city_name = InitDao.getCityNameById(dis_id);
				city_id   = dis_id;
				Map<String, String> pa = InitDao.getProvinceListByCityId(dis_id);
				if (pa != null && pa.size() > 0) {
					province_id   = pa.get("id");
					province_name = pa.get("name");
				}
			}
		} else if (DF_CITY.equals(dis_id)) {
			province_id = DF_CITY;
			province_name = "全国";
		} else {
			if (StringUtils.isNotBlank(this.urlService.getRtParameter().get(RT))) {
				context.set("province_name", "全国");
			}
		}
		context.set("city_id", city_id);
		context.set("city_name", city_name);
		context.set("province_id", province_id);
		context.set("province_name", province_name);
		// 会考虑城市排序问题
		// return cityURLMap;
	}

	/**
	 * 处理seo的title,keyword,description和相关搜索
	 */
	public void dealRelatedSearchAndSeoData(Map<String, String> solrQueryParameter, MyContext context) {
		String title = "";
		String keywords = "";
		String description = "";

		String key = this.urlService.getKeyWorksMap().get("key");// 是否是热门关键词搜索keywords/baisejiaju/这样的搜索
		String keyword = this.urlService.getKeyWorksMap().get("searchKeyword");// 页面表单关键词搜索
																				// xxx.html?keywords=白色家具这样的搜索
		String cat_id = (String) solrQueryParameter.get("cat_id");

		boolean addSelectedSeo = true;

		/** 热门搜索关键词搜索 start */
		if (cat_id != null && !"null".equals(cat_id)) {
			Map<String, Object> tempMap = getBaseRelatedAndSeoDataMap(solrQueryParameter, context);
			title = (String) tempMap.get("title");
			keywords = (String) tempMap.get("keywords");
			description = (String) tempMap.get("description");

			/** 设置当前位置 */
			if (StringUtils.isNotEmpty(keyword)) {// 如果有搜索关键字,则加载URL末尾
				String k_url_u = keyword;
				try {
					k_url_u = URLEncoder.encode(k_url_u, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				String k_url = "mcat0-scat0-b0-max0-min0-attr.html?keywords=" + k_url_u;
				context.set("sub_category_url", tempMap.get("sub_category_url") + k_url);
				context.set("parent_category_url", tempMap.get("parent_category_url") + k_url);
			} else {
				// context.set("sub_category_url",
				// tempMap.get("sub_category_url"));
				context.set("parent_category_url", tempMap.get("parent_category_url"));
				if (StringUtils.isNotEmpty((String) tempMap.get("sub_category_url"))
						&& !tempMap.get("sub_category_url").equals(tempMap.get("parent_category_url"))) {
					context.set("sub_category_url", tempMap.get("sub_category_url"));
				}
			}
			if (StringUtils.isNotEmpty((String) tempMap.get("sub_category")) && !tempMap.get("sub_category").equals(tempMap.get("parent_category"))) {
				context.set("sub_category", tempMap.get("sub_category"));
			}
			// context.set("sub_category", tempMap.get("sub_category"));
			context.set("parent_category", tempMap.get("parent_category"));
		} else {
			// 如果查询参数中有品牌信息，就取品牌的seo信息
			String brandId = solrQueryParameter.get("brand_id");
			if (StringUtils.isNotEmpty(brandId)) {
				Map<String, String> brandSeoMap = InitSeoDao.getBrandSeoMap(brandId);
				if (brandSeoMap != null && brandSeoMap.size() == 3) {
					addSelectedSeo = false;
					title = brandSeoMap.get("title");
					keywords = brandSeoMap.get("keywords");
					description = brandSeoMap.get("description");
				}
			}
			// 如果标题为空就取公共的标题
			if (StringUtils.isEmpty(title)) {
				title = InitSeoDao.getPublicSeoInfoByName("title");
				if (key != null) {
					KeywordsInfo ki = InitDao.getKeywordsInfoByName(key);
					if (ki != null) {
						keyword = ki.getKeyword();
						keywords = ki.getRelated_keyword();
						description = ki.getDes();
						/** 关键词 */
						if (keyword != null && !"*".equals(keyword)) {
							keywords = keyword + " " + keywords;
						}
					}
				} else {
					keywords = InitSeoDao.getPublicSeoInfoByName("keyword");
					description = InitSeoDao.getPublicSeoInfoByName("description");
				}
			}
		}

		/** 若用户自定义了title,keyords,description等信息则取自定义信息 */
		String isKeywordsCat = (String) this.urlService.getKeyWorksMap().get("isKeywordsCat");
		if (isKeywordsCat != null) {
			String keywords_pinyin = this.urlService.getKeyWorksMap().get("keywords_pinyin");
			HotKeywordsSeo seo = InitSeoDao.getHotKeywordsSeoByPinyin(keywords_pinyin);
			if (seo != null && seo.getKeywords() != null && !"".equals(seo.getKeywords().trim())) {
				keywords = seo.getKeywords();
			}
			if (seo != null && seo.getDes() != null && !"".equals(seo.getDes().trim())) {
				description = seo.getDes();
			}
			if (seo != null && seo.getTitle() != null && !"".equals(seo.getTitle().trim())) {
				title = seo.getTitle();
			} else if (seo != null && StringUtils.isBlank(seo.getTitle())) {
				if (StringUtils.isNotEmpty(StringUtil.html2Text(keyword))) {
					title = keyword + "-" + title;
				}
			}
		} else if (StringUtils.isNotBlank(keyword)) {
			if (StringUtils.isNotEmpty(StringUtil.html2Text(keyword))) {
				title = StringUtil.html2Text(keyword) + "-" + title;
				keywords = StringUtil.html2Text(keyword) + "-" + keywords;
				description = StringUtil.html2Text(keyword) + "-" + description;
			}
		}
		StringBuffer sb = new StringBuffer();
		for (String selectValue : this.selectList) {
			sb.append(selectValue + "-");
		}
		context.set("keywords", addSelectedSeo ? sb.toString() + StringUtil.html2Text(keywords) : keywords);
		context.set("description", description);
		String seoTitle = addSelectedSeo ? sb.toString() + title : title;
		context.set("title", seoTitle.replace("-null", "").replace("null-", ""));
		/** 热门搜索关键词搜索 end */
	}

	private Map<String, Object> getBaseRelatedAndSeoDataMap(Map<String, String> solrQueryParameter, MyContext context) {
		String cat_id = solrQueryParameter.get("cat_id");
		Map<String, Object> map = new HashMap<String, Object>();
		String title = "";
		String keywords = "";
		String description = "";
		String catName = Cats.getCatNameById(cat_id);
		UsualTitleInfo uti = InitSeoDao.getUsualTitleByCatId(cat_id);
		if (uti != null && StringUtils.isNotBlank(uti.getKeywords()) && StringUtils.isNotBlank(uti.getDescription())) {
			keywords = uti.getKeywords();
			description = uti.getDescription();
			title = catName + "-" + InitSeoDao.getPublicSeoInfoByName("title");
			/** 设置当前位置 */
			map.put("sub_category", uti.getCatName());
		}
		map.put("sub_category_url", "/category-" + Cats.getPinyinById(cat_id) + "/");

		String parent_id = Cats.getParentIdById(cat_id);
		if (!"0".equals(parent_id) && !"1".equals(parent_id)) {
			map.put("parent_category", Cats.getCatNameById(parent_id));
			title = catName + "-" + Cats.getCatNameById(parent_id) + "-" + InitSeoDao.getPublicSeoInfoByName("title");
			map.put("parent_category_url", "/category-" + Cats.getPinyinById(parent_id) + "/");
		}

		Map<String, String> catSeoMap = new HashMap<String, String>();
		boolean is_manual = ifManualSeo(cat_id, catName, catSeoMap);
		if (is_manual) {
			context.set("is_manual", "true");
			map.put("title", catSeoMap.get("title"));
			map.put("keywords", catSeoMap.get("keyword"));
			map.put("description", catSeoMap.get("description"));
		} else {
			context.set("is_manual", "false");
			map.put("title", title);
			map.put("keywords", keywords);
			map.put("description", description);
		}
		return map;
	}

	private static boolean ifManualSeo(String cat_id, String cat_name, Map<String, String> catSeoMap) {
		boolean is_manual = false;
		Map<String, String> sMap = InitSeoDao.getSeoInfoByCatId(cat_id);
		if (sMap != null && sMap.size() > 0) {
			String title = sMap.get("title");
			String keyword = sMap.get("keyword");
			String description = sMap.get("description");
			String regex = "((\\ \\r\\n)*\\[(A|B|C|D|E|F)\\.[\\ \u4E00-\u9FA5]+\\](\\ \\r\\n)*)+";
			String regex_title = getTokens(title, regex);
			String regex_keyword = getTokens(keyword, regex);
			String regex_description = getTokens(description, regex);
			if (regex_title.length() < title.length() && regex_keyword.length() < keyword.length() && regex_description.length() < description.length()) {// 说明有设置固定属性之外的seo内容,说明是手动设置
				String tmpt = title.replaceAll("(\\[E.分类\\])+", cat_name);
				tmpt = tmpt.replaceAll("\\[(A|B|C|D|E|F|Z)\\.[\\ \u4E00-\u9FA5]+\\]", "");
				String tmpk = keyword.replaceAll("(\\[E.分类\\])+", cat_name);
				tmpk = tmpk.replaceAll("\\[(A|B|C|D|E|F|Z)\\.[\\ \u4E00-\u9FA5]+\\]", "");
				// tmpk = tmpk.replaceAll("(\\[C.标号\\])+", "");
				String tmpd = description.replaceAll("(\\[E.分类\\])+", cat_name);
				tmpd = tmpd.replaceAll("\\[(A|B|C|D|E|F|Z)\\.[\\ \u4E00-\u9FA5]+\\]", "");
				// 替换掉特殊的符号
				tmpt = tmpt.replaceAll("[\\ ]+", "");
				tmpt = tmpt.replaceAll("[-]+", "-");
				tmpt = tmpt.replaceAll("^-", "-");
				tmpk = tmpk.replaceAll("[\\ ]+", "");
				tmpk = tmpk.replaceAll("[-]+", "-");
				tmpk = tmpk.replaceAll("^-", "-");
				tmpd = tmpd.replaceAll("[\\ ]+", "");
				tmpd = tmpd.replaceAll("[-]+", "-");
				tmpd = tmpd.replaceAll("^-", "-");

				catSeoMap.put("title", tmpt);
				catSeoMap.put("keyword", tmpk);
				catSeoMap.put("description", tmpd);
				is_manual = true;
			}
		}
		return is_manual;
	}

	public static String getTokens(String formula, String reg) {
		Pattern expP = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher expM = expP.matcher(formula);
		// List<String> expList = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		while (expM.find()) {
			sb.append(expM.group());
		}
		return sb.toString();
	}

	/**
	 * 处理分页数据
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void dealSolrPage(long totalSize, Map<String, String> solrQueryParameter, MyContext context) {
		Long startIndex = Long.parseLong((String) solrQueryParameter.get("startPage"));
		Long pageSize = Long.parseLong((String) solrQueryParameter.get("pageSize"));
		Map<String, Object> tempsMap = doPageDatas(totalSize, startIndex, String.valueOf(pageSize));
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		Set<String> sets = tempsMap.keySet();
		for (String str : sets) {
			pagerMap.put(str, tempsMap.get(str));
		}
		// context.set("num_founds", eachCount);
		context.set("max_page", tempsMap.get("page_count"));
		int currentPage = Integer.parseInt(String.valueOf(tempsMap.get("page")));
		int pages = Integer.parseInt(String.valueOf(tempsMap.get("page_count")));
		int dopage_url_next = 0;
		int dopage_url_previous = 0;
		if (currentPage < pages) {
			dopage_url_next = currentPage + 1;
		} else {
			dopage_url_next = currentPage;
		}
		if (currentPage > 1) {
			dopage_url_previous = currentPage - 1;
		} else {
			dopage_url_previous = currentPage;
		}
		List doPageUrlList = (ArrayList) tempsMap.get("array");
		for (int i = 0; i < doPageUrlList.size(); i++) {

			Map<String, String> pageM = new HashMap<String, String>();
			pageM.put("url", urlService.buildUrl("page", String.valueOf(doPageUrlList.get(i))));
			pageM.put("page_num", String.valueOf(doPageUrlList.get(i)));
			doPageUrlList.set(i, pageM);
		}
		context.set("dopage_url_list", doPageUrlList);
		context.set("current_page", tempsMap.get("page"));
		context.set("dopage_url_next", urlService.buildUrl("page", String.valueOf(dopage_url_next)));
		context.set("dopage_url_previous", urlService.buildUrl("page", String.valueOf(dopage_url_previous)));
		context.set("dopage_url_first", urlService.buildUrl("page", String.valueOf(1)));
		context.set("dopage_url_end", urlService.buildUrl("page", String.valueOf(pages)));
		context.set("page_count", tempsMap.get("page_count"));
		context.set("record_count", tempsMap.get("record_count"));
		context.set("goto_page_url", urlService.buildUrl("page", String.valueOf(1)));
		// 处理分页参数
		Map<String, Object> pager = new HashMap<String, Object>();
		pager.put("record_count", tempsMap.get("record_count"));
		// pager.put("pageSize",String.valueOf(pageSize));
		// pager.put("page",tempsMap.get("page"));
		pager.put("page_count", tempsMap.get("page_count"));
		// pager.put("start",String.valueOf((currentPage-1)*pageSize));
		// pager.put("array", doPageUrlList);
		context.set("pager", pager);
		tempsMap = null;
		doPageUrlList = null;
	}

	/** 获取分页数据 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map<String, Object> doPageDatas(long count, long startsPage, String rows) {
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		pagerMap.put("record_count", count);

		/** 从多少条开始 */
		/** 一页显示的条数 */
		int pageSize = Integer.parseInt(rows);
		pagerMap.put("start", startsPage * pageSize);
		pagerMap.put("pageSize", pageSize);

		/** 计算一共多少页 */
		long pages = count % pageSize;
		if (pages == 0) {
			pages = count / pageSize;
		} else {
			pages = count / pageSize + 1;
		}

		pagerMap.put("page_count", pages);
		/** 当前页 */
		pagerMap.put("page", startsPage);

		ArrayList pagesNumList = new ArrayList();

		int allPages = 9;
		long currentPage = startsPage;
		long lower = 0;
		long higher = 0;
		if (currentPage <= 5) {
			if (pages <= allPages) {
				lower = 1;
				higher = pages;
			} else {
				lower = 1;
				higher = allPages;
			}
		} else if (currentPage <= 100) {
			if (pages - 4 >= currentPage) {
				lower = currentPage - 4;
				higher = currentPage + 4;
			} else {
				lower = currentPage - 4;
				higher = pages;
			}
		} else {
			if (pages - 3 >= currentPage) {
				lower = currentPage - 3;
				higher = currentPage + 3;
			} else {
				lower = currentPage - 3;
				higher = pages;
			}
		}
		for (long p = lower; p <= higher; p++) {
			pagesNumList.add(p);
		}
		pagerMap.put("array", pagesNumList);
		return pagerMap;
	}

	/**
	 * 单独处理cat facet字段
	 */
	public void dealCatFacetData(MyContext context, List<FacetField> facetResult) {
		Map<String, Object> resultMap = null;
		boolean is_select_condition = false;
		List<Map<String, String>> static_list = null;
		int time = 0;
		for (FacetField fecetField : facetResult) {
			resultMap = new HashMap<String, Object>();
			List<Count> facetCount = fecetField.getValues();
			String id = null;
			String fieldId = null;
			String[] countArray = null;
			Map<String, String> paramsMap = null;
			static_list = new ArrayList<Map<String, String>>();
			for (Count count : facetCount) {
				countArray = count.getName().split(",");
				id = countArray[0];
				paramsMap = new HashMap<String, String>();
				paramsMap.put("count", String.valueOf(count.getCount()));
				paramsMap.put("name", countArray[1]);
				paramsMap.put("core_id", id);
				fieldId = solrUrlParameter.get("cat_id");
				paramsMap.put("url", "/category-" + countArray[2] + "/");
				static_list.add(paramsMap);
				// 设置选中参数
				if (StringUtils.isNotEmpty(fieldId)) {
					if (id.equals(fieldId)) {
						is_select_condition = true;
						resultMap.put("select_core_id", id);
						resultMap.put("select_core_name", countArray[1]);
						// selectList.add(countArray[1]);
					}
				}

			}
			if (static_list.size() > 0) {
				resultMap.put("core_list", static_list);
				resultMap.put("name", "分类");
				resultMap.put("value", "cat_id");
				resultMap.put("all_core_cat_url", "/category-" + Cats.getPinyinById(Cats.getParentIdById(id)) + "/");
				String show_type = Cats.getShowTypeById(static_list.get(0).get("name"));
				if (StringUtils.isNotBlank(show_type)) {
					context.set("cat_show_type", show_type);
					// context.set("_show_type", show_type);
				}
				context.set("catArray", resultMap);
				context.set("is_select_condition", is_select_condition ? "yes" : "");// 分类选中,也会显示
			}
			time++;
		}
	}

	/**
	 * 处理facet数据，并设置选中的facet字段
	 */
	@SuppressWarnings("rawtypes")
	public void dealFacetData(Map<String, Object> solrFactParameter, MyContext context, List<FacetField> facetResult) {
		List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		boolean is_select_condition = false;
		List<Map<String, Object>> static_list = null;
		String catId = solrUrlParameter.get("cat_id");
		int index = 0, maxSelectParamRows = 0;// 最大选择行号,从上往下变大
		// 处理排序结果，需要对每个属性进行竖向排序
		boolean isFacetSelected=false;
		
		for (FacetField fecetField : facetResult) {
		    isFacetSelected=false;
			List<Count> facetCount = fecetField.getValues();
			if (facetCount.size() == 0) {
				continue;
			}
			resultMap = new HashMap<String, Object>();
			String facetName = fecetField.getName();
			if (baseFacetField.contains(facetName)) {
				String id = null;
				String fieldId = null;
				String[] countArray = null;
				Map<String, Object> paramsMap = null;
				if ("mdname".equals(facetName)) {
                    resultMap.put("name", "材质");
                    resultMap.put("value", "material_id");
                    resultMap.put("all_core_cat_url", urlService.buildUrl("mcat", "0"));
                } else if ("sdname".equals(facetName)) {
                    resultMap.put("name", "风格");
                    resultMap.put("value", "style_id");
                    resultMap.put("all_core_cat_url", urlService.buildUrl("scat", "0"));
                } else if ("bdname".equals(facetName)) {
                    resultMap.put("name", "品牌");
                    resultMap.put("value", "series_id");
                    String brandUrl = urlService.buildUrl("b", "0");
                    if (brandUrl.contains("brandId=")) {
                        brandUrl = brandUrl.replaceAll("brandId=\\d{1,4}", "");
                    }
                    resultMap.put("all_core_cat_url", brandUrl);
                    // 获取每个分类的排序字段
                }
				static_list = new ArrayList<Map<String, Object>>();
				for (Count count : facetCount) {
					countArray = count.getName().split(",");
					if (countArray.length == 1) {// 材质,风格等,都可以为空
						continue;
					}
					id = countArray[0];
					paramsMap = new HashMap<String, Object>();
					paramsMap.put("count", String.valueOf(count.getCount()));
					paramsMap.put("name", countArray[1]);
					paramsMap.put("core_id", id);
					int attr_sort = 0;
					// 处理每个分类的排序
					String attr_name = "";
					if ("mdname".equals(facetName)) {
						fieldId = solrUrlParameter.get("material_id");
						attr_name = "material_id";
						paramsMap.put("url", urlService.buildUrl("mcat", id));
					} else if ("sdname".equals(facetName)) {
						fieldId = solrUrlParameter.get("style_id");
						attr_name = "style_id";
						paramsMap.put("url", urlService.buildUrl("scat", id));
					} else if ("bdname".equals(facetName)) {
						fieldId = solrUrlParameter.get("brand_id");
						attr_name = "series_id";
						paramsMap.put("url", urlService.buildUrl("b", id));
						if(StringUtils.isNotEmpty(InitDao.getBrandImageUrlByBrandId(id))){
							paramsMap.put("brand_logo",InitDao.getBrandImageUrlByBrandId(id));
						}
					}
					if (StringUtils.isNotEmpty(catId)) {
					    //System.out.println("aaa");
						String sortKey = catId + "-" + InitFacetDao.getAttrIdByRelationId(attr_name + "-" + id);
						attr_sort = InitFacetDao.getCatAttrSortValue(sortKey);
						//System.out.println(sortKey+":"+attr_sort);
					}
					paramsMap.put("sort", attr_sort);
					if (attr_sort != -1) {
						static_list.add(paramsMap);
					}
					// static_list.add(paramsMap);
					// 设置选中参数
					if (StringUtils.isNotEmpty(fieldId)) {
						if (id.equals(fieldId)) {
							is_select_condition = true;
							isFacetSelected=true;
							resultMap.put("select_core_id", id);
							resultMap.put("select_core_name", resultMap.get("name")+": "+countArray[1]);
							selectList.add(countArray[1]);
							maxSelectParamRows = index;
						}
					}
				}
				// 对list进行排序
				Collections.sort(static_list, new Comparator<Map<String, Object>>() {
					public int compare(Map<String, Object> o1, Map<String, Object> o2) {
						int sort1 = (Integer) o1.get("sort");
						int sort2 = (Integer) o2.get("sort");
						if (sort1 < sort2) {
							return 1;
						} else {
							return -1;
						}
					}
				});
				resultMap.put("core_list", static_list);
				
			} else {
				String name = InitFacetDao.getPropertyNameByDbName(facetName);
				resultMap.put("name", name);
				resultMap.put("value", facetName);
				// 对ar_开始的动态属性进行处理
				resultMap.put("core_count", String.valueOf(fecetField.getValueCount()));
				resultMap.put("all_core_cat_url", urlService.buildDelDynamicUrl(facetName));
				List<Map<String, Object>> core_list = new ArrayList<Map<String, Object>>();
				for (Count count : facetCount) {// 品牌的时候,不能到Property里边取名称,因为id和分类等有重复
					Map<String, Object> coreDataMap = new HashMap<String, Object>();
					coreDataMap.put("core_id", count.getName());
					// 动态属性的自定义排序，新增功能,根据后台提供的排序字段进行排序
					int attr_sort = 0;
					if (StringUtils.isNotEmpty(catId)) {
						String sortKey = catId + "-" + count.getName();
						attr_sort = InitFacetDao.getCatAttrSortValue(sortKey);
					}
					coreDataMap.put("sort", attr_sort);
					coreDataMap.put("count", String.valueOf(count.getCount()));
					String propertyName = InitFacetDao.getPropertyNameById(count.getName());
					if (propertyName.contains("其他") || propertyName.contains("其它")) {
						coreDataMap.put("sort", -1);
					}
					coreDataMap.put("name", propertyName);
					coreDataMap.put("url", urlService.buildDynamicUrl(facetName, count.getName()));
					if (StringUtils.isNotEmpty(solrUrlParameter.get(facetName))) {
						if (count.getName().equals(solrUrlParameter.get(facetName))) {
							is_select_condition = true;
							isFacetSelected=true;
							resultMap.put("select_core_id", count.getName());
							String select_core_name = InitFacetDao.getPropertyNameById(count.getName());
							resultMap.put("select_core_name", resultMap.get("name")+": "+select_core_name);
							// 设置动态属性的seo信息
							selectList.add(select_core_name);
							maxSelectParamRows = index;
						}
					}
					if (attr_sort != -1) {
						core_list.add(coreDataMap);
					}
				}
				Collections.sort(core_list, new Comparator<Map<String, Object>>() {
					public int compare(Map<String, Object> o1, Map<String, Object> o2) {
						int sort1 = (Integer) o1.get("sort");
						int sort2 = (Integer) o2.get("sort");
						if (sort1 < sort2) {
							return 1;
						} else {
							return -1;
						}
					}
				});
				resultMap.put("core_list", core_list);
			}
			String facetValue = (String) resultMap.get("value");
			String facetId = InitFacetDao.getCatAttrIdByDbName(facetValue);
			int attr_sort = 0;
			if (StringUtils.isNotEmpty(catId) && StringUtils.isNotEmpty(facetId)) {
				String sortKey = catId + "-" + facetId;
				attr_sort = InitFacetDao.getCatSortValue(sortKey);
			}
			resultMap.put("sort", attr_sort);
			//
			if (attr_sort != -1) {
				// 判断是否有具体的属性值
				if (resultMap.get("core_list") != null && ((List) resultMap.get("core_list")).size() > 0) {
					propertyList.add(resultMap);
				}
			}
			index++;
		}
		// 合并价格数据，进行统一排序
		Map<String, Object> priceMap = dealPriceData(catId, context,solrFactParameter);
		if(priceMap.get("core_list")!=null){
		    if("1".equals(priceMap.get("isDiyPrice"))){
		        propertyList.add(priceMap);
		    }else{
		        if(((List) priceMap.get("core_list")).size()>0){
		            propertyList.add(priceMap);
		        }
		    }
		   
		}
		
		// 对facet数据进行排序
		Collections.sort(propertyList, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				int sort1 = (Integer) o1.get("sort");
				int sort2 = (Integer) o2.get("sort");
				if (sort1 < sort2) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		// 对整个产品的属性，进行竖排排序
		context.set("propertyList", propertyList);
		// 提取5~7位置的属性,进行更多选项的提示
		String moreOptions = "";
		int realIndex=0;
		int selectedCount=0;
		for (int i = 0; i < propertyList.size(); i++) {
		   
			if (realIndex >= 4 &&selectedCount<3 && StringUtils.isEmpty((String) propertyList.get(i).get("select_core_id"))) {
				moreOptions += (String) propertyList.get(i).get("name") + ",";
				selectedCount++;
			}
			 if(StringUtils.isEmpty((String) propertyList.get(i).get("select_core_id"))){
	                realIndex++;
	            }
		}
		if (StringUtils.isNotEmpty(moreOptions)) {
			if (moreOptions.endsWith(",")) {
				moreOptions = moreOptions.substring(0, moreOptions.length() - 1);
			}
			context.set("moreOptions", moreOptions);
		}
		String is_cat_select = String.valueOf(context.getValueMap().get("is_select_condition"));
		if (!"yes".equals(is_cat_select)) {
			context.set("is_select_condition", is_select_condition ? "yes" : "no");
		}
		context.set("totalProSize", propertyList.size() + 2);
		if(context.getValueMap().get("is_midbar_fold")==null){
			context.set("is_midbar_fold", maxSelectParamRows > 0 ? 1 : 0);
		}
	}

	public void dealRelatedSearch(Map<String, String> solrQueryParameter, MyContext context) throws Exception {
		String keywords = solrQueryParameter.get("keywords");
		if (StringUtils.isNotEmpty(keywords)) {
			String  keywordsCat  = InitOtherDao.getKeywordsCatId(keywords);
			boolean isMllKeyword = StringUtils.isNotBlank(keywordsCat) && !"0".equals(keywordsCat);
			keywords=ClientUtils.escapeQueryChars(keywords);
			SolrServer server = SolrServerFactory.getSolrServerInstance(SysConfig.getValue("hotKeywordsRequestUrl"));
			SolrQuery  query  = new SolrQuery();
			if (isMllKeyword) {
				query.set("q", keywords + " AND cat_id : " + keywordsCat);
			} else {
				query.set("q", keywords + " AND total_search_counts: [10 TO *] ");
			}
			query.setSortField("total_search_counts", ORDER.desc);
			query.set("start", "0");
			query.set("rows", "11");
			QueryResponse queryReponse = server.query(query);
			SolrDocumentList documents = queryReponse.getResults();
			if (isMllKeyword && documents.size() < 5) {
				StringBuilder buf = new StringBuilder();
				for (int i = 0; i < documents.size(); i++) {
					buf.append("-id : ").append(documents.get(i).get("id")).append(" AND ");
				}
				query.set("q", buf.append("keywords : " + keywords + " AND is_in_mll_dictionary : true").toString());
				query.set("rows", 5 - documents.size());
				queryReponse = server.query(query);
				documents.addAll(queryReponse.getResults());
			}
			Collections.shuffle(documents);
			List<HotKeywords> keywordsList  = new ArrayList<HotKeywords>();
			Iterator<SolrDocument> iterator = documents.iterator();
			SolrDocument document    = null;
			HotKeywords  hotKeywords = null;
			String       docKeywords = null;
			String       kwPinyin    = null;
			boolean isInMllDictionary= false;
			while (iterator.hasNext()) {
				document    = iterator.next();
				docKeywords = (String) document.get("keywords");
				// 去除本身关键字
				if (solrQueryParameter.get("keywords").equals(docKeywords)) {
					continue;
				}
				isInMllDictionary = (Boolean) document.get("is_in_mll_dictionary");
				kwPinyin    = (String) document.get("pinyin");
				hotKeywords = new HotKeywords();
				hotKeywords.setName(docKeywords);
				if (isInMllDictionary) {
					hotKeywords.setUrl("/keywords/" + kwPinyin + "/");
				} else {
					hotKeywords.setUrl("/category-9999/mcat0-scat0-b0-max0-min0-attr-page-1-sort-sort_order-order-asc.html?keywords="
							+ URLEncoder.encode(docKeywords, "UTF-8") + "&from=r");
				}
				keywordsList.add(hotKeywords);
			}
			Collections.shuffle(keywordsList);
			context.set("relation_search", keywordsList.size() > 5 ? keywordsList.subList(0, 5) : keywordsList);
		}
	}

	/**
	 * 处理查询关键字,为关键在二次分词作准备
	 */
	public void dealKeyWords(Map<String, String> solrQueryParameter, MyContext context) {
		if (StringUtils.isNotEmpty(solrQueryParameter.get("keywords"))) {
			context.set("keyword", solrQueryParameter.get("keywords"));
		}
	}

	public void dealOtherParameter(Map<String, String> solrQueryParameter, MyContext context, String requestUrl) {

        StringBuilder canonicalBuffer = new StringBuilder("/");
		String cat_id = solrQueryParameter.get("cat_id");
		if (requestUrl.contains("keywords=")) {
			canonicalBuffer.append("category-9999/"+urlService.getBaseMappingUrl()).append(requestUrl.split(".html")[1]);
			context.set("canonical",     canonicalBuffer.toString());
			context.set("re_select_url", canonicalBuffer.toString());
			if (StringUtils.isNotEmpty(solrQueryParameter.get("keywords"))) {
				String catId = Cats.getCatIdByName(solrQueryParameter.get("keywords"));
				if (StringUtils.isNotBlank(catId)) {
					context.set("mainCatID", Cats.getIsParentById(catId) ? cat_id : Cats.getParentIdById(catId));
					context.set("_show_type", Cats.getShowTypeById(catId));
				}
			}
		} else if (StringUtils.isNotBlank(cat_id)) {
			context.set("mainCatID", Cats.getIsParentById(cat_id) ? cat_id : Cats.getParentIdById(cat_id));
			context.set("_show_type", Cats.getShowTypeById(cat_id));
			context.set("select_cat_pinyin", Cats.getPinyinById(cat_id));
			canonicalBuffer.append("category-").append(Cats.getPinyinById(cat_id)).append("/");
			context.set("re_select_url", canonicalBuffer.toString());
			context.set("canonical",     canonicalBuffer.toString());
		} else {
			if (requestUrl.contains("/keywords/")) {
				String keywords = StringUtils.substringBetween(requestUrl, "/keywords/", "/");
				canonicalBuffer.append("keywords/").append(keywords).append("/");
			} else {
				Matcher matcher = BRAND_PATTERN.matcher(requestUrl);
				if (matcher.find()) {
					String brandId = StringUtils.substringBetween(matcher.group(), "b", "-");
					canonicalBuffer.append("brand-").append(brandId).append("/");
				} else {
					canonicalBuffer.append("category-9999/");
				}
			}
			context.set("canonical",     canonicalBuffer.toString());
			context.set("re_select_url", "/category-9999/");
		} 
		context.set("basic_url", urlService.buildUrl("city", "xxxx"));
		// 设置现货url, 把默认的00改为city0, 0代表全国
		String rtUrl = null;
		if (requestUrl.contains("-rt1-")) {
			rtUrl = urlService.buildUrl("rt", String.valueOf(0));
		} else {
			rtUrl = urlService.buildUrl("rt", String.valueOf(1));
		}
		if (rtUrl.contains("city00")) {
			rtUrl = rtUrl.replace("city00", "city0");
		}
		context.set("rt_url", rtUrl.replaceAll("-lt\\d{1,}", ""));
	}
	
	private static final Pattern BRAND_PATTERN = Pattern.compile("-b[1-9]\\d*-");

	public void dealDeliveryData(MyContext context) {
		List<Map<String, String>> deliverysArray = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		for (Activity activity : deliverys) {
			map = new HashMap<String, String>();
			map.put("name", activity.getName());
			map.put("value", activity.getValue());
			map.put("url", urlService.buildDeliveryUrl(activity.getValue()));
			deliverysArray.add(map);
			// 设置活动的选中字段，如果活动类型中已经有活动地址了，就取消原来的活动地址
			if (solrUrlParameter.get("delivery_type") != null && solrUrlParameter.get("delivery_type").equals(activity.getValue())) {
				context.set("select_delivery", map);
				// 设置选中的价格到选中list
				selectList.add(activity.getName());
			}

		}
		context.set("deliverys", deliverysArray);
		
	}

	public void dealHotIndex(Map<String, String> solrQueryParameter,
			MyContext context) {
		String catId=solrQueryParameter.get("cat_id");
		List<String> hotIndexList=new ArrayList<String>();
		hotIndexList.add(0,"风格");
		hotIndexList.add(1,"材质");
		// 如果是品牌，直接走建材热门索引
		if(StringUtils.isNotEmpty(solrQueryParameter.get("brand_id"))){
			hotIndexList.add(0,"建材");
			context.set("hotIndex", hotIndexList);
			return; 
		}
		
		// 处理分类的热门索引
		if(StringUtils.isNotEmpty(catId)){
			// 获取分类需要显示的热门索引
			String show_type=Cats.getShowTypeById(catId);
			if("0".equals(show_type)){
				hotIndexList.add(0,"家具");
			}else if("1".equals(show_type)){
				hotIndexList.add(0,"建材");
			}else{
				hotIndexList.add(0,"家饰");
			}
			context.set("hotIndex", hotIndexList);
			return;
		}
	   String searchKeywords=solrQueryParameter.get("keywords");
	   if(StringUtils.isEmpty(searchKeywords)){
		   return;
	   }
	   if(InitFacetDao.isFurnitureHotIndex(searchKeywords)){
		   hotIndexList.add(0,"家具");
		   context.set("hotIndex", hotIndexList);
		   return; 
	   } 
	   if(InitFacetDao.isBuildingMaterialsHotIndex(searchKeywords)){
		   hotIndexList.add(0,"建材");
		   context.set("hotIndex", hotIndexList);
		   return; 
	   }
	   // 如果是按品牌分，就走建材的热门索引
	   if(InitFacetDao.isBrandName(searchKeywords)){
		   hotIndexList.add(0,"建材");
		   context.set("hotIndex", hotIndexList);
		   return;
	   }
	   
	   hotIndexList.add(0,"家饰");
	   context.set("hotIndex", hotIndexList);
	   return;
	   // 处理关键字的热门索引信息
	}
}
