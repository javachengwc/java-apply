package com.solr.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网站配置文件
 */
public class Config {
	private String serverName;
	private List<UrlParameter> urlParameter = new ArrayList<UrlParameter>();
	private List<Price> priceList = new ArrayList<Price>();
	private List<String> facetField = new ArrayList<String>();
	private List<Activity> activitys = new ArrayList<Activity>();
	private List<String> sortList = new ArrayList<String>();
	// 默认排序字段
	private Map<String, String> defaultSort = new HashMap<String, String>();
	// 服务的其他配置参数
	private Map<String, String> serviceParameter = new HashMap<String, String>();

	public Config() {
	}
	
	public Config(String serverName) {
		this.serverName = serverName;
	}
	
	public final List<UrlParameter> getUrlParameter() {
		return urlParameter;
	}

	public final void setUrlParameter(List<UrlParameter> urlParameter) {
		this.urlParameter = urlParameter;
	}

	public final List<Price> getPriceList() {
		return priceList;
	}

	public final void setPriceList(List<Price> priceList) {
		this.priceList = priceList;
	}

	public final List<String> getFacetField() {
		return facetField;
	}

	public final void setFacetField(List<String> facetField) {
		this.facetField = facetField;
	}

	public final List<Activity> getActivitys() {
		return activitys;
	}

	public final void setActivitys(List<Activity> activitys) {
		this.activitys = activitys;
	}

	public final List<String> getSortList() {
		return sortList;
	}

	public final void setSortList(List<String> sortList) {
		this.sortList = sortList;
	}

	public final Map<String, String> getDefaultSort() {
		return defaultSort;
	}

	public final void setDefaultSort(Map<String, String> defaultSort) {
		this.defaultSort = defaultSort;
	}

	public final Map<String, String> getServiceParameter() {
		return serviceParameter;
	}

	public final void setServiceParameter(Map<String, String> serviceParameter) {
		this.serviceParameter = serviceParameter;
	}

	public final String getServerName() {
		return serverName;
	}

	public final void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
