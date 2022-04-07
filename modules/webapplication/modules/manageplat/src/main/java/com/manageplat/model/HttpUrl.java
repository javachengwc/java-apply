package com.manageplat.model;

import com.util.http.UrlUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

public class HttpUrl {

	protected String url;
	
	protected String domain;
	
	protected int port=80;
	
	protected Map<String,String> header;
	
	protected String queryString;
	
	protected Map<String,String> paramMap;
	
	public HttpUrl(String url)
	{
		this.url=url;
		queryString = UrlUtil.getQueryString(this.url);
		paramMap= UrlUtil.getParamMap(this.url);
		port = UrlUtil.getPort(this.url);
		domain =UrlUtil.getDomain(this.url);
	}
	
	public HttpUrl(String url, Map<String, String> header)
	{
		this(url);
		this.header=header;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	
	public String getQueryString() {
		return queryString;
	}

	public Map<String, String> getParamMap() {
		return paramMap;
	}
	
	public String getParam(String param)
	{
		 if(paramMap==null)
		 {
			 return null;
		 }
		 return paramMap.get(param);
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
	
}
