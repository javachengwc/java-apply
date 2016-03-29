package com.solr.service.ru;

import com.solr.initialize.InitRuDao;
import com.solr.model.Price;
import com.solr.model.UrlParameter;
import com.solr.util.KeywordsUtil;
import com.solr.util.ServiceCfg;
import com.solr.service.IUrlService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuUrlService extends IUrlService {

	public static String serviceName="ru";
	/**判断url是否已0-0-0-0-0-0.html*/
	private static final int paramLength = 6;
	private static final int sortAndPageLength=3;
	private static final String urlRegex = "(\\d{1,}-){1,5}(\\d{1,})";
	private static final Pattern urlParamerPattern=Pattern.compile(urlRegex);
	private static final String urlNoParamRegex = "/category";
	private static final Pattern urlNoParamPattern =Pattern.compile(urlNoParamRegex);
	
	private List<Integer> queryParameters = new ArrayList<Integer>();
	private static List<UrlParameter> urlParams= ServiceCfg.getUrlParameterByName(serviceName);
	private static List<String> sortFields= ServiceCfg.getSortFieldsByName(serviceName);
	private static List<Price> priceList = ServiceCfg.getPriceListByName(serviceName);
	private Map<String, String> keyWorksMap=new HashMap<String,String>();
	private long priceBegin = 0;
	private long priceEnd = 0;
	
	public RuUrlService(){
	}
	
	public final void setSolrRequestUrl(String requestUrl) {//
		solrRequestUrl = ServiceCfg.getServiceParameterByName(serviceName).get("solrRequestUrl");
	}
	
	public final void parseUrlParameter(String url) {
		fetchQueryParameters(url);
		setUrlParameters();
		setKeywords();
	}


	public void genRealServerName(HttpServletRequest req)
	{
		String serverName=serviceName;
		String url = req.getRequestURI();
		int start = url.indexOf("/");
		if(start>=0)
		{
			url = url.substring(start+1);
			int end = url.indexOf("/");
			if(end>0)
			{
				url=url.substring(0,end);
			}
			serverName =url;
		}
		this.setServiceName(serverName);
	}
	
	
	private final void setKeywords() {
		String keyword="";
		if(StringUtils.isNotEmpty(solrParameter.get("keywords"))){
			keyWorksMap.put("key",solrParameter.get("keywords"));
			keyWorksMap.put("keywords_pinyin",solrParameter.get("keywords"));
			keyWorksMap.put("isKeywordsCat","yes");
		}
		if(StringUtils.isNotEmpty(searchKeywords)){
			keyWorksMap.put("searchKeyword",searchKeywords);
			keyword=searchKeywords;
		}
		if(StringUtils.isNotEmpty(keyword)){
		   solrParameter.put("keywords", keyword);
		}
	}

	private final void fetchQueryParameters(String url) {
		Matcher matcher=urlParamerPattern.matcher(url);
		String urlParameter="";
		if(matcher.find()){
			urlParameter=matcher.group();
		}
		String[] urlParams = urlParameter.split("-");
		for(int i=0;i<paramLength;i++){//不足补足
			if(null!=urlParams&&i<urlParams.length){
				queryParameters.add(Integer.valueOf(urlParams[i]));
			}else{
				queryParameters.add(0);
			}
		}
		
		if(url.contains("/keywords/")){
			urlParameter = url.replaceAll("/(\\d{1,}-){1,5}(\\d{1,}).html", "");
			urlParameter = urlParameter.replace("/keywords/", "");
			String hotKeywords = InitRuDao.getKeywordsByAcronym(urlParameter);
			if(StringUtils.isBlank(hotKeywords)){
				hotKeywords = urlParameter;
			}
			setSearchKeywords(hotKeywords);
		}
	}

	private final void setUrlParameters() {
		//1-2(主,子分类),3-价格 4-5(排序,升降),6(页数)
		for(int i=0;i<urlParams.size();i++){
			UrlParameter param = urlParams.get(i);
			if("query".equals(param.getType())&&queryParameters.get(i)!=0){
				String id = String.valueOf(queryParameters.get(i));
				
				solrParameter.put(param.getSolrname(), id);
				
			}else if("sort".equals(param.getType())){
				if(0==queryParameters.get(i)){
					solrSortParameter.put("sort", ServiceCfg.getDefaultSortByName(serviceName).get("sort"));
				}else{
					solrSortParameter.put("sort", sortFields.get(queryParameters.get(i)-1));
				}
			}else if("order".equals(param.getType())){
				if(0==queryParameters.get(i)){
					solrSortParameter.put("order", ServiceCfg.getDefaultSortByName(serviceName).get("order"));
				}else{
					solrSortParameter.put("order", queryParameters.get(i)==1?"asc":"desc");
				}
			}else if("price".equals(param.getType())&&queryParameters.get(i)>=1){
				Price price = priceList.get(queryParameters.get(i)-1);
				priceBegin = price.getBegin();
				priceEnd = price.getEnd();
				solrParameter.put(param.getSolrname(), "["+price.getBegin()+" TO "+price.getEnd()+"]");
			}else if("page".equals(param.getType())){
				if(queryParameters.get(i)==0){
					solrPageParameter.put("page", String.valueOf(1));
				}else{
					solrPageParameter.put("page", String.valueOf(queryParameters.get(i)));
				}
			}
		}
		if(null!=solrPageParameter.get("page")&&!"0".equals(solrPageParameter.get("page"))){
			solrPageParameter.put("startPage", solrPageParameter.get("page"));
		}else{
			solrPageParameter.put("startPage", String.valueOf(1));
		}
		solrPageParameter.put("pageSize", ServiceCfg.getServiceParameterByName(serviceName).get("pageSize"));
	}
	
	public final void buildFacetParameter() {
		List<String> facetFields = ServiceCfg.getFacetFieldByName(serviceName);
		solrFacetParameter.addAll(facetFields);
	}
	/**
	 * 获取url里面的排序参数
	 */
	public final Map<String, String> buildSolrSortParameter() {
		Map<String, String> solrMap = new LinkedHashMap<String, String>();
		//如果url里面没有排序字段，就用默认的排序字段
		if(StringUtils.isEmpty(solrSortParameter.get("sort"))||"sort_order".equals(solrSortParameter.get("sort"))){
			solrMap.put(ServiceCfg.getDefaultSortByName(serviceName).get("sort"), ServiceCfg.getDefaultSortByName(serviceName).get("order"));
		}else{
			solrMap.put(solrSortParameter.get("sort"), solrSortParameter.get("order"));
		}
		return solrMap;
	}
	
	public final String buildNumUrl(int index,String value){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<queryParameters.size();i++){
			if(i == index){
				sb.append(value+"-");
			}else{
				sb.append(queryParameters.get(i)+"-");
			}
		}
		if(sb.toString().endsWith("-")){
			sb.deleteCharAt(sb.length()-1);
		}
		return requestUrl.replaceAll(urlRegex,sb.toString());
	}
	
	public final String buildTabUrl(int index,String value,String defValue){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<queryParameters.size();i++){
			if(i == index){
				sb.append(value+"-");
			}else if(i >= (paramLength-sortAndPageLength)){
				sb.append(defValue+"-");
			}
			else{
				sb.append(queryParameters.get(i)+"-");
			}
		}
		if(sb.toString().endsWith("-")){
			sb.deleteCharAt(sb.length()-1);
		}
		String rt= requestUrl.replaceAll(urlRegex,sb.toString());
		
		int i = rt.indexOf("?");
		if(i>=0)
		{
		    rt = rt.substring(0,i);
		}
		return rt;
	}
	
	public final String buildNumUrl(int index,String value,boolean hasParam){
		String rt = buildNumUrl(index,value);
		if(!hasParam)
		{
			int i = rt.indexOf("?");
			if(i>=0)
			{
			    rt = rt.substring(0,i);
			}
		}
		return rt;
	}
	
	public final String build2ParamUrl(int index,String ivalue,int jndex,String jvalue){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<queryParameters.size();i++){
			if(i == index){
				sb.append(ivalue+"-");
			}else if(i == jndex){
				sb.append(jvalue+"-");
			}else{
				sb.append(queryParameters.get(i)+"-");
			}
		}
		if(sb.toString().endsWith("-")){
			sb.deleteCharAt(sb.length()-1);
		}
		return requestUrl.replaceAll(urlRegex,sb.toString());
	}

	public final String buildResetUrl(int index,String ivalue){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<queryParameters.size();i++){
			if(i == index){
				sb.append(ivalue+"-");
			}else{
				sb.append("0-");
			}
		}
		if(sb.toString().endsWith("-")){
			sb.deleteCharAt(sb.length()-1);
		}
		return requestUrl.replaceAll(urlRegex,sb.toString());
	}
	
	public final String buildResetUrl(){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<queryParameters.size();i++){
			sb.append("0-");
		}
		if(sb.toString().endsWith("-")){
			sb.deleteCharAt(sb.length()-1);
		}
		return requestUrl.replaceAll(urlRegex,sb.toString());
	}
	
	public Map<String, Object> getSolrFacetParameter() {
		Map<String, Object> solrFacetField = new HashMap<String, Object>();
		solrFacetField.put("facet.field", solrFacetParameter);
		// facet的查询字段,按分类和按照keyword两个方向,如果是品牌的，把品牌的查询字段放入query中
		String catId = solrParameter.get("cat_id");
		String keywords = solrParameter.get("keywords");
		StringBuffer sb = new StringBuffer();
		String search_type = null;
		if(StringUtils.isNotEmpty(keywords)){//cat_id+keywords
			String regex = "[0-9a-zA-Z#-]+";
    		if(keywords.matches(regex)){
    			search_type = "goods_sn";
    		}
			keywords = KeywordsUtil.transformSolrMetacharactor(keywords);
			if(StringUtils.isNotEmpty(catId)){
				sb.append("cat_id:").append(catId).append(" AND text:").append(keywords);
			}else{
				int i=0;
				for(Map.Entry<String, String> entry:solrParameter.entrySet()){
					if("keywords".equals(entry.getKey())){
						continue;
					}else{
						sb.append(entry.getKey()+":"+entry.getValue());
					}
					if(i<solrParameter.size()-1){
						sb.append(" AND ");
					}
					i++;
				}
				sb.append(keywords);
			}
		}else{
			if (StringUtils.isNotEmpty(catId)) {
				sb.append("cat_id:").append(catId);
			} else {
				int i=0;
				for(Map.Entry<String, String> entry:solrParameter.entrySet()){
					sb.append(entry.getKey()+":"+entry.getValue());
					if(i<solrParameter.size()-1){
						sb.append(" AND ");
					}
					i++;
				}
			}
		}
		if(!"goods_sn".equals(search_type)){
			if(!StringUtils.isEmpty(sb.toString())){
				sb.append(" AND ");
			}
			sb.append("is_show_web:true");
        }
		solrFacetField.put("facet.query", sb.toString());
		return solrFacetField;
	}
	
	public final long getPriceBegin(){
		return priceBegin;
	}
	
	public final long getPriceEnd(){
		return priceEnd;
	}
	
	public final String getKeywordsByName(String name){
		return keyWorksMap.get(name);
	}
	
	public String getRequestUrl()
	{
		return requestUrl;
	}
	
	public String getPrefixRequestUrl()
	{
		String prefix=null;
		Matcher m = urlParamerPattern.matcher(requestUrl);
		if(m.find())
		{
			String aa = m.group();
			prefix = requestUrl.substring(0,requestUrl.indexOf(aa));
		}
		else {
			Matcher m2 = urlNoParamPattern.matcher(requestUrl);
			if(m2.find())
			{
				String bb =m2.group();
				int index =requestUrl.indexOf(bb);
				if(index>0)
				{
				   prefix = requestUrl.substring(0,index)+bb;
				}else
				{
					prefix = bb;
				}
			}else
			{
				prefix ="/ru/category/";
			}
		}
		if(!prefix.endsWith("/"))
		{
			prefix+="/";
		}
		return prefix;
	}
}