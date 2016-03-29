package com.solr.service.impl;

import com.solr.initialize.Cats;
import com.solr.initialize.InitDao;
import com.solr.initialize.InitFacetDao;
import com.solr.model.UrlParameter;
import com.solr.service.IUrlService;
import com.solr.util.KeywordsUtil;
import com.solr.util.ServiceCfg;
import com.solr.util.UrlTool;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlService extends IUrlService {

	private String						brandId;
	private static String				serverName		= "default";
	private static List<UrlParameter>	result			= ServiceCfg.getUrlParameterByName(serverName);
	private Map<String, String>			keyWorksMap		= new HashMap<String, String>();
	protected Map<String, String>		priceMap		= new HashMap<String, String>();
	protected Map<String, String>		rtParameter		= new HashMap<String, String>();
	private String						baseMappingUrl	= "mcat0-scat0-b0-max0-min0-city00-rt0-attr-page-1-sort-sort_order-order-asc.html";
	//不参数facet的查询字段
	private static final List<String> notAddFacetQueryParameter=new ArrayList<String>();
	static{
	    //活动晒全
	    notAddFacetQueryParameter.add("activity_search_name");
	    //配送方式
	    notAddFacetQueryParameter.add("delivery_type");
	    //活动标签
	    notAddFacetQueryParameter.add("is_activity");
	}
   

	public UrlService() {
		super.setServiceName(serverName);
	}

	/**
	 * 进行url匹配转换
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public final String buildUrl(String name, String value) {
		UrlParameter urlParameter = null;
		for (UrlParameter parameter : result) {
			if (name.equals(parameter.getName())) {
				urlParameter = parameter;
				break;
			}
		}
		return requestUrl.replaceAll(urlParameter.getExpression(), urlParameter.getSepartor() + value);
	}

	public final String buildSortUrl(String name, String value, String defaultOrder, Map<String, String> solrSortParameter) {
		UrlParameter urlParameter = null;
		for (UrlParameter parameter : result) {
			if (name.equals(parameter.getName())) {
				urlParameter = parameter;
				break;
			}
		}
		String order = defaultOrder;
		//如果当前排序字段和需要设置的字段相等，就取排序的相反属性.否则取默认排序
		if (value.equals(solrSortParameter.get("sort"))) {
			order = "asc".equals(solrSortParameter.get("order")) ? "desc" : "asc";
		}
		//构建排序url
		String sortOrderUrl = requestUrl.replaceAll(urlParameter.getExpression(), urlParameter.getSepartor() + value);
		sortOrderUrl = buildUrl(sortOrderUrl, "order", order);
		return sortOrderUrl;
	}

	public final String buildUrl(String requestUrl, String name, String value) {
		UrlParameter urlParameter = null;
		for (UrlParameter parameter : result) {
			if (name.equals(parameter.getName())) {
				urlParameter = parameter;
				break;
			}
		}
		return requestUrl.replaceAll(urlParameter.getExpression(), urlParameter.getSepartor() + value);
	}

	public final String buildDynamicUrl(String dynamicName, String dynamicValue) {
		String dynamicId = InitFacetDao.getPropertyHtmlNameByDbName(dynamicName);
		String dynamicUrl = null;
		if (requestUrl.contains("-" + dynamicId + "_")) {//添加之前,检查是否已有属性组中的属性,如果有,则直接替换,否则直接添加
			if (!requestUrl.contains("-" + dynamicId + "_" + dynamicValue)) {//如果已有完全同样的属性值,则不做任何处理
				dynamicUrl = requestUrl.replaceAll("(-" + dynamicId + "_(\\d+)-)+", "-" + dynamicId + "_" + dynamicValue + "-");
			} else {
				dynamicUrl = requestUrl;
			}
		} else {
			int addDynamicIndex = requestUrl.indexOf("-max");
			dynamicUrl = requestUrl.substring(0, addDynamicIndex) + "-" + dynamicId + "_" + dynamicValue
					+ requestUrl.substring(addDynamicIndex);
		}
		return dynamicUrl;
	}

	public final String buildDelDynamicUrl(String dynamicName) {
		String id = InitFacetDao.getPropertyHtmlNameByDbName(dynamicName);
		String dynamicUrl = null;
		if (StringUtils.isNotBlank(id)) {
			dynamicUrl = requestUrl.replaceAll("(-" + id + "_(\\d+)-)+", "-");
		}
		return dynamicUrl;
	}

	public final String buildActivityUrl(String activityName) {
		UrlParameter urlParameter = null;
		for (UrlParameter parameter : result) {
			if ("activity_name".equals(parameter.getName())) {
				urlParameter = parameter;
				break;
			}
		}
		String activityBaseUrl = requestUrl.replaceAll("-" + urlParameter.getExpression() + "-1", "");

		// add by xiaopinglu
		if ("all".equals(activityName)) {
			return activityBaseUrl;
		}

		if (requestUrl.contains(activityName + "-1")) {
			return activityBaseUrl;
		}
		int activityIndex = activityBaseUrl.indexOf(".html");
		String dynamicUrl = activityBaseUrl.substring(0, activityIndex) + "-" + activityName + "-1"
				+ activityBaseUrl.substring(activityIndex);
		return dynamicUrl;
	}
	
	public final String buildDeliveryUrl(String activityValue) {
		
		String activityBaseUrl = requestUrl.replaceAll("-dt\\d", "");

		// add by xiaopinglu
		if ("0".equals(activityValue)) {
			return activityBaseUrl;
		}

		if (requestUrl.contains("dt"+activityValue)) {
			return activityBaseUrl;
		}
		int activityIndex = activityBaseUrl.indexOf(".html");
		String dynamicUrl = activityBaseUrl.substring(0, activityIndex) + "-dt" + activityValue
				+ activityBaseUrl.substring(activityIndex);
		return dynamicUrl;
	}
    public final String buildActivitysUrl(String activityValue) {
		
		String activityBaseUrl = requestUrl.replaceAll("-act\\d", "");

		// add by xiaopinglu
		if ("0".equals(activityValue)) {
			return activityBaseUrl;
		}

		if (requestUrl.contains("act"+activityValue)) {
			return activityBaseUrl;
		}
		int activityIndex = activityBaseUrl.indexOf(".html");
		String dynamicUrl = activityBaseUrl.substring(0, activityIndex) + "-act" + activityValue
				+ activityBaseUrl.substring(activityIndex);
		return dynamicUrl;
	}

	/**
	 * 获取并且格式化URL
	 * 
	 * @param req
	 * @param isDecode
	 *            是否解码
	 * @return
	 * @throws Exception
	 */
	public String getAndformatUrl(HttpServletRequest req, boolean isDecode) {
		String url = decode(req.getRequestURI());
		url=url.replaceAll("(\\'.*\\')|(\'.*)|(\".*)|(\\<.*)|(\\>.*)","");
		//如果url中有‘xxx’特殊字符的特殊字符，去除其中的特殊字符
		if (!url.contains(".html")) {
			if (!url.endsWith("/")) {
				url += "/";
			}
		}
		url = url.replace("/json", "");
		//对品牌从特殊处理
		Matcher m = Pattern.compile("brand-(\\d{1,})").matcher(url);
		while (m.find()) {
			//operate=m.group().split("-")[1];
			url = url.replaceAll("brand-(\\d{1,})", "category-9999");
			brandId = m.group().replace("brand-", "");
			solrParameter.put("brand_id", brandId);
			//把从品牌页面过来的参数放入url中
			baseMappingUrl = baseMappingUrl.replaceAll("b(\\d{1,})", "b" + brandId) + "?brandId=" + brandId;
		}
		StringBuffer urlAppend = new StringBuffer(url);
		try {
			String qs = req.getQueryString();
			if (isDecode) {
				qs = decode(qs);
			}//解码
			if (!url.contains(".html")) {
				urlAppend.append(baseMappingUrl);
			}
			if (!url.contains("-rt") && url.contains("-attr")) {
				urlAppend = urlAppend.insert(url.indexOf("-attr"), "-rt0");
			}
			if (!url.contains("-city") && url.contains("-attr")) {
				urlAppend = urlAppend.insert(url.indexOf("-attr"), "-city00");
			}
			if (StringUtils.isNotBlank(qs)) {
				qs=qs.replaceAll("level=\\d{1,}&","").replaceAll("\\&level=\\d{1,}","").replaceAll("level=\\d{1,}","");
				if(StringUtils.isNotBlank(qs)){
				 urlAppend.append("?").append(UrlTool.utf8UrlEncode(qs.replaceAll("(\".*)|(\'.*)|(\\<.*)|(\\>.*)","")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urlAppend.toString();
	}

	/**
	 * 获取并且格式化URL,考虑如果url地址不够长,则补充到足够长度
	 * 
	 * @param req
	 * @param isD
	 *            是否解码
	 * @return
	 * @throws Exception
	 */
	public String getAndformatUrl(HttpServletRequest req, int length, boolean isD) {
		String url = req.getRequestURI();
		StringBuffer sb = new StringBuffer();
		try {
			String qs = req.getQueryString();
			if (isD) {
				qs = decode(qs);
			}// 解码
			if (!url.endsWith(".html")) {
				sb.append(url);
				for (int i = 0; i < length - 1; i++) {
					sb.append("0-");
				}
				sb.append("0.html");
			} else {
				sb.append(url);
			}
			if (StringUtils.isNotBlank(qs)) {
				sb.append("?").append(qs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 验证url地址是否正确，留给子类覆盖，实现一些url参数的验证
	 * 
	 * @param url
	 * @return
	 */
	public final boolean isValidateUrl(String url) {
		return true;
	}

	/**
	 * 根据url解析url里面的参数
	 * 
	 * @param url
	 */
	public void parseUrlParameter(String url) {
		String priceBegin = "0";
		String priceEnd = "0";
		for (UrlParameter parameter : result) {
			Matcher m = Pattern.compile(parameter.getExpression()).matcher(url);
			while (m.find()) {
				// System.out.println(m.group());
				if (parameter.getName().equals("dynamic")) {
					String html_param_name = m.group().split(parameter.getSepartor())[0];
					String name = InitFacetDao.getPropertDbNameByHtmlName(html_param_name);
					//					String name = "ar_" + m.group().split(parameter.getSepartor())[0];
					String value = m.group().split(parameter.getSepartor())[1];
					if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value)) {
						solrParameter.put(name, value);
					}
				} else {
					String value = "";
					if (parameter.getSepartor() != null) {
						value = m.group().replaceAll(parameter.getSepartor(), "");
					} else {
						value = m.group();
					}
					//没有值的不放入参数列表
					if (StringUtils.isEmpty(value)) {
						continue;
					}
					if ("0".equals(value)) {
						//只有价格和city可以为0
						if (!(parameter.getSolrname().equals("effect_price") || parameter.getSolrname().equals("city"))) {
							continue;
						}
					}
					//city默认00不放入参数。
					if ("00".equals(value)) {
						continue;
					}

					// 把查询字段、排序字段、分页字段分开
					if (parameter.getSolrname().equals("sort")) {
						if (ServiceCfg.getSortFieldsByName(serverName).contains(value)) {
							solrSortParameter.put(parameter.getSolrname(), value);
						} else {
							solrSortParameter.put(parameter.getSolrname(), "sort_order");
						}
					} else if (parameter.getSolrname().equals("order")) {
						if ("desc".equalsIgnoreCase(value) || "asc".equalsIgnoreCase(value)) {
							solrSortParameter.put(parameter.getSolrname(), value);
						} else {
							solrSortParameter.put(parameter.getSolrname(), "desc");
						}
					} else if (parameter.getType().equals("page")) {
						solrPageParameter.put(parameter.getSolrname(), value);
					} else {
						//只有min的值可以为0
						if (parameter.getName().equals("min")) {
							priceBegin = value;
							priceMap.put("min", value);
							continue;
						}
						if (parameter.getName().equals("max")) {
							priceEnd = value;
							priceMap.put("max", value);
							continue;
						}
						if (parameter.getName().equals("category") && "9999".equals(value)) {
							continue;
						}
						solrParameter.put(parameter.getSolrname(), value);
					}

				}
			}
		}
		//对价格进行特殊处理

		if (!"0".equals(priceEnd) || !"0".equals(priceBegin)) {
			if (!"0".equals(priceEnd)) {
				solrParameter.put("effect_price", "[" + priceBegin + " TO " + priceEnd + "]");
			} else {
				solrParameter.put("effect_price", "[" + priceBegin + " TO * ]");
			}
		}
		//keword进行处理
		String keyword = "";
		if (StringUtils.isNotEmpty(solrParameter.get("keywords"))) {
			keyword = InitDao.getKeywordsByPinyin(solrParameter.get("keywords"));
			keyWorksMap.put("key", solrParameter.get("keywords"));
			//设置keyword相关属性
			keyWorksMap.put("keywords_pinyin", solrParameter.get("keywords"));
			keyWorksMap.put("isKeywordsCat", "yes");
			//
		}
		if (StringUtils.isNotEmpty(searchKeywords)) {
			keyWorksMap.put("searchKeyword", searchKeywords);
			keyword = searchKeywords;
		}
		if (StringUtils.isNotEmpty(keyword)) {
			solrParameter.put("keywords", keyword);
		}
		if (StringUtils.isNotEmpty(solrParameter.get("rt"))) {
			rtParameter.put("rt", solrParameter.get("rt"));
		}
		String districtId = solrParameter.get("city");

		solrParameter.remove("lt");

        if (StringUtils.isNotEmpty(solrParameter.get("activity_search_name"))) {
			solrParameter.put("activity_search_name", "*," + solrParameter.get("activity_search_name") + ",*");
		}
	}

	public void buildPageSizeToParameter(int pageSize) {
		//动态设置pageSize
		solrPageParameter.put("pageSize", String.valueOf(pageSize));
		//默认从第一页开始
		if (StringUtils.isEmpty(solrPageParameter.get("startPage"))) {
			solrPageParameter.put("startPage", String.valueOf(1));
		}
	}

	/**
	 * 根据url信息构建facet参数
	 */
	public void buildFacetParameter() {
		// 配置的facet字段
		String catId = solrParameter.get("cat_id");
		ArrayList<Integer> catFacetField = null;
		if (StringUtils.isNotBlank(catId)) {
			catFacetField = InitFacetDao.getFacetIdListByCatId(catId);
		}
		if (catFacetField == null) {
			solrFacetParameter.addAll(ServiceCfg.getFacetFieldByName(serverName));
		} else {
			// 动态的facet字段,如果是正式环境,则需要修改差值为5,field = field-5
			String staticParamName = null;
			for (int field : catFacetField) {
				staticParamName = InitFacetDao.getStaticParamsNameById(field);
				if ("品牌".equals(staticParamName)) {
					solrFacetParameter.add("bdname");
				} else if ("材质".equals(staticParamName)) {
					solrFacetParameter.add("mdname");
				} else if ("风格".equals(staticParamName)) {
					solrFacetParameter.add("sdname");
				} else {
					String dbName = InitFacetDao.getPropertDbNameByHtmlName(String.valueOf(field));
					if (StringUtils.isNotBlank(dbName)) {
						solrFacetParameter.add(dbName);
					}
				}
			}
		}
	}

	public void buildCategoryId() {
		String categoryPinyin = solrParameter.get("cat_id");
		if (StringUtils.isEmpty(categoryPinyin)) {
			return;
		}
		// 从初始化参数中根据拼音获取id
		String catId = Cats.getIdByPinyin(categoryPinyin);
		if (StringUtils.isNotEmpty(catId)) {
			solrParameter.put("cat_id", catId);
		} else {
			solrParameter.remove("cat_id");
		}
	}

	/**
	 * 获取url里面的排序参数
	 */
	public Map<String, String> buildSolrSortParameter(String cityId) {
		Map<String, String> solrMap = new LinkedHashMap<String, String>();
		//如果url里面没有排序字段，就用默认的排序字段
		if (StringUtils.isBlank(solrParameter.get("keywords"))) {//如果有搜索关键词,则默认按照相关度排序
			if (StringUtils.isEmpty(solrSortParameter.get("sort")) || "sort_order".equals(solrSortParameter.get("sort"))) {
				if(StringUtils.isNotEmpty(cityId) && cityId.trim().length()>0 && InitDao.isContainsSortField("level_"+cityId)){
					solrMap.put("level_"+cityId, "asc");
				}
				solrMap.put("first_page", "asc");
				solrMap.put("cat_sort", "asc");
				solrMap.put(ServiceCfg.getDefaultSortByName(serverName).get("sort"),
						ServiceCfg.getDefaultSortByName(serverName).get("order"));
			} else {
				solrMap.put(solrSortParameter.get("sort"), solrSortParameter.get("order"));
			}
		} else {
			if (!"sort_order".equals(solrSortParameter.get("sort"))) {
				solrMap.put(solrSortParameter.get("sort"), solrSortParameter.get("order"));
			}else{
				solrMap.put("score","desc");
			}
		}
		return solrMap;
	}

	/**
	 * 获取solr的request url;
	 * 
	 * @return
	 */
	public String getSolrRequestUrl() {
		return ServiceCfg.getServiceParameterByName(serverName).get("solrRequestUrl");
	}

	public Map<String, Object> getSolrFacetParameter(Map<String, String> specialSolrQuertParameter) {

	    Map<String, Object> solrFacetField = new HashMap<String, Object>();
		solrFacetField.put("facet.field", solrFacetParameter);
		
		
		// facet的查询字段,按分类和按照keyword两个方向,如果是品牌的，把品牌的查询字段放入query中
		//String catId = solrParameter.get("cat_id");
		String keywords = specialSolrQuertParameter.get("keywords");
		String search_type = "normal";
		StringBuffer sb = new StringBuffer();
		int fieldNumber=0;
		if (StringUtils.isNotEmpty(keywords)) {//cat_id+keywords
			String regex = "[0-9a-zA-Z#-]+";
			if (keywords.matches(regex)) {
				search_type = "goods_sn";
			}
			keywords = KeywordsUtil.transformSolrMetacharactor(keywords);
			 if("goods_sn".equals(search_type)){
                 sb.append(keywords);
                 sb.append(" AND ");
             }else{
                 solrFacetField.put("facet.flag","main");
                 solrFacetField.put("facet.keywords",keywords);
             }
			  int i = 0;
               for (Map.Entry<String, String> entry : specialSolrQuertParameter.entrySet()) {
               if(notAddFacetQueryParameter.contains(entry.getKey())){
                   continue;
               }    
               if ("rt".equals(entry.getKey())) {
                   //sb.append(entry.getValue());
                   continue;
                } else if ("keywords".equals(entry.getKey()) || "flag".equals(entry.getKey())) {
                   continue;
               }  else if ("cityQuery".equals(entry.getKey())) {
                   sb.append(entry.getValue());
               }else if ("tl".equals(entry.getKey())) {
                   //sb.append(entry.getValue());
                   continue;
               }else {
                   sb.append(entry.getKey() + ":" + entry.getValue());
                   fieldNumber++;
               }
               if (i < specialSolrQuertParameter.size() - 1) {
                   sb.append(" AND ");
               }
               i++;
             }
			
		} else {
		    
          int i = 0;
          for (Map.Entry<String, String> entry : specialSolrQuertParameter.entrySet()) {
              if(notAddFacetQueryParameter.contains(entry.getKey())){
                  continue;
              }   
              if ("rt".equals(entry.getKey())) {
                // sb.append(entry.getValue());
                continue;
              } else if ("cityQuery".equals(entry.getKey())) {
                  sb.append(entry.getValue());
              }else if ("tl".equals(entry.getKey())) {
                  //sb.append(entry.getValue());
                  continue;
              } else {
                  sb.append(entry.getKey() + ":" + entry.getValue());
              }
              if (i < specialSolrQuertParameter.size() - 1) {
                  sb.append(" AND ");
              }
              i++;
          }

		}
		solrFacetField.put("facet.number", fieldNumber);
		solrFacetField.put("facet.query", sb.toString());
		return solrFacetField;
	}

	public String getBaseMappingUrl() {
		return baseMappingUrl;
	}

	public void setBaseMappingUrl(String baseMappingUrl) {
		this.baseMappingUrl = baseMappingUrl;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setSolrRequestUrl(String solrRequestUrl) {
		this.solrRequestUrl = solrRequestUrl;
	}

	public Map<String, String> getRtParameter() {
		return rtParameter;
	}

	public Map<String, String> getPriceMap() {
		return priceMap;
	}

	public Map<String, String> getKeyWorksMap() {
		return keyWorksMap;
	}

	public String getSearchKeywords() {
		return searchKeywords;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

}
