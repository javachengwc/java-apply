package com.solr.web.action.response;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class Return extends JSONObject{

	private static final long serialVersionUID = -592295115459L;
	
	protected static String DEFAULT_RESULT_STR ="result";
	
	protected static String DEFAULT_MSG_STR="msg";
    
	public static Return Success = new Return(0);
	
	public Return()
	{
		
	}
	
	public Return(Map<String,String> map)
	{
		for(String key:map.keySet())
		{
			this.put(key, map.get(key));
		}
	}
	
	public Return(int result)
	{
		this.put(DEFAULT_RESULT_STR, result);
	}
	
	public Return(int result,String msg)
	{
		this.put(DEFAULT_RESULT_STR, result);
		this.put(DEFAULT_MSG_STR, msg);
	}
}
