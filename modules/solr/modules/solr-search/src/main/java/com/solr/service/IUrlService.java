package com.solr.service;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * url解析接口
 */
public abstract class IUrlService {
	
	protected String serverName;//项目url前缀
	protected String requestUrl;//项目url请求地址
	protected String solrRequestUrl;//项目solr请求地址
	protected String searchKeywords;//项目搜索关键词
	
	protected List<String> solrFacetParameter = new ArrayList<String>();
	protected Map<String, String> solrParameter = new HashMap<String, String>();
	protected Map<String, String> solrSortParameter = new HashMap<String, String>();
	protected Map<String, String> solrPageParameter = new HashMap<String, String>();
	
	/**
	 * 组装并特殊处理url地址
	 * @param req http 请求
	 * @param length url参数个数
	 * @param isD 是否解码url地址
	 * @return
	 */
	public String getAndformatUrl(HttpServletRequest req, int length, boolean isD){
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
	 * 组装并特殊处理url地址
	 * @param req http 请求
	 * @param serverName 项目url前缀
	 * @param isDecode 是否解码url地址
	 * @return
	 */
	public String getAndformatUrl(HttpServletRequest req,String serverName, boolean isDecode){
		String url = req.getRequestURI();
		if (!url.contains(".html")) {
			if (!url.endsWith("/")) {
				url += "/";
			}
		}
		url = url.replace("/" + serverName, "");
		StringBuffer urlAppend = new StringBuffer(url);
		String qs = req.getQueryString();
		if (isDecode) {
			qs = decode(qs);
		}// 解码
		if (StringUtils.isNotBlank(qs)) {
			urlAppend.append("?").append(qs);
		}
		return urlAppend.toString();
	}
	
	/**
	 * 组装并特殊处理url地址
	 * @param req http 请求
	 * @param serverName 项目url前缀
	 * @param length url参数个数
	 * @param isDecode 是否解码url地址
	 * @return
	 */
	public String getAndformatUrl(HttpServletRequest req,String serverName,int length, boolean isDecode){
		String url = req.getRequestURI();
		if (!url.contains(".html")) {
			if (!url.endsWith("/")) {
				url += "/";
			}
		}
		url = url.replace("/" + serverName, "");
		StringBuffer urlAppend = new StringBuffer();
		if (!url.endsWith(".html")) {
			urlAppend.append(url);
			for (int i = 0; i < length - 1; i++) {
				urlAppend.append("0-");
			}
			urlAppend.append("0.html");
		} else {
			urlAppend.append(url);
		}
		String qs = req.getQueryString();
		if (isDecode) {
			qs = decode(qs);
		}// 解码
		if (StringUtils.isNotBlank(qs)) {
			urlAppend.append("?").append(qs);
		}
		return urlAppend.toString();
	}
	
	/**
	 * 根据url解析url里面的参数
	 */
	public abstract void parseUrlParameter(String url);

	/**
	 * 设置facet查询参数
	 */
	public void buildFacetParameter(){};
	/**
	 * 设置页面产品个数
	 * @param pageSize
	 */
	public void buildPageSizeToParameter(int pageSize){}
	/**
	 * 构建url
	 * @param name 字段名称
	 * @param value 字段值
	 * @return
	 */
	public String buildUrl(String name,String value){
		return null;
	}
	/**
	 * 构建url
	 * @param srcUrl 原始url
	 * @param name 字段名称
	 * @param value 字段值
	 * @return
	 */
	public String buildUrl(String srcUrl,String name,String value){
		return null;
	}
	/**
	 * 主站构建动态url地址
	 * @param dynamicName
	 * @param dynamicValue
	 * @return
	 */
	public String buildDynamicUrl(String dynamicName, String dynamicValue){
		return null;
	}
	/**
	 * 构建删除动态属性的url地址
	 * @param dynamicName
	 * @return
	 */
	public String buildDelDynamicUrl(String dynamicName) {
    	return null;
	}
	/**
	 * 构建主站相关活动url地址
	 * @param activityName
	 * @return
	 */
	public String buildActivityUrl(String activityName) {
		return null;
	}
	/**
	 * 构建主站分类url地址
	 */
	public void buildCategoryId() {
		// do nothing...
	}
	
	/**
	 * 设置排序url
	 * @param name 字段名称
	 * @param value 字段值
	 * @param defaultOrder 默认排序
	 * @param solrSortParameter url中的排序参数
	 * @return
	 */
	public String buildSortUrl(String name,String value,String defaultOrder,Map<String, String> solrSortParameter){
		return null;
	}
	
	/**
	 * 解码
	 * @param qs
	 * @return
	 */
	protected final String decode(String qs){
		if (StringUtils.isNotBlank(qs)) {
			try {
				qs = URLDecoder.decode(qs, "UTF-8");
			} catch (Exception e) {
				qs = "\"\"";// 如果存在不能转码的数据,则直接赋值为"";
			}
		}
		return qs;
	}
	
	/**
	 * 验证url地址是否正确，留给子类覆盖，实现一些url参数的验证
	 * 
	 * @param url
	 * @return
	 */
	public boolean isValidateUrl(String url) {
		return true;
    }
	
	/**
	 * 设置搜索关键词
	 * @param searchKeywords
	 */
	public final void setSearchKeywords(String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}
	/**
	 * 设置请求url地址
	 * @param requestUrl
	 */
	public final void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	public final void setServiceName(String serviceName){
		this.serverName = serviceName;
	}
	
	/**
	 * 把url里面的参数转化为参数map
	 */
	public final Map<String, String> getSolrQueryParameter() {
		return solrParameter;
	}
	/**
	 * 获取solr,facet参数,用于筛选条件的设置
	 * @return
	 */
	public Map<String, Object> getSolrFacetParameter() {
		return null;
	}
	
	/**
	 * 获取url里面的排序参数
	 */
	public final Map<String, String> getSolrSortParameter() {
		return solrSortParameter;
	}
	
	public final Map<String, String> getSolrPageParameter() {
		return solrPageParameter;
	}
	
	public final String getServiceName(){
		return serverName;
	}
}