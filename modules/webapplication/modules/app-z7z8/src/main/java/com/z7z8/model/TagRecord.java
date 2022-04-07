package com.z7z8.model;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class TagRecord {
	
	private Integer id;
	
	private String url;
	
	private String tag;
	
	private int matchType;//matchType 1--精确，2--模糊
	
	private Integer siteType;
	
	private Pattern urlPattern;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getMatchType() {
		return matchType;
	}

	public void setMatchType(int matchType) {
		this.matchType = matchType;
		
	}
	
	public Pattern getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(Pattern urlPattern) {
		this.urlPattern = urlPattern;
	}

	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}

	public void producePattern()
	{
		if(matchType==2 && !StringUtils.isBlank(url) && urlPattern==null)
		{
			if(url.startsWith("/") && url.endsWith("/"))
			{
				String ptnStr=url.substring(1,url.length()-1);
				urlPattern = Pattern.compile(ptnStr);
			}
			else
			{
			    urlPattern = Pattern.compile(url);
			}
		}
	}

	public Integer getSiteType() {
		return siteType;
	}

	public void setSiteType(Integer siteType) {
		this.siteType = siteType;
	}
	
}
