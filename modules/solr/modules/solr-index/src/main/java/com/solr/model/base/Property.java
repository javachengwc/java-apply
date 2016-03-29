package com.solr.model.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;


@SuppressWarnings("serial")
public class Property extends HashMap<String, String>
{
	public int getInt(String key)
	{
		return Integer.parseInt(this.get(key));
	}
	
	public long getLong(String key)
	{
		return Long.parseLong(this.get(key));
	}
	
	public boolean getBoolean(String key){
		Object o = this.get(key);
		if(o != null)
        {
            if(o.equals(Boolean.FALSE) || (o instanceof String) && ((String)o).equalsIgnoreCase("false"))
                return false;
            if(o.equals(Boolean.TRUE) || (o instanceof String) && ((String)o).equalsIgnoreCase("true"))
                return true;
        }
		return false;
	}
	
	public long getLong(String key, long defaultValue)
	{
		long ret = defaultValue;
		try
		{
			ret = Long.parseLong(this.get(key));
		}
		catch(Exception e)
		{
			// TODO: handle exception
		}
		return ret;
		
	}
	
	public String put(String key, Number value)
	{
		return this.put(key, String.valueOf(value));
	}
	
	public Date getDate(String key)
	{
		if(null == key)
		{
			return null;
		}
		String value = this.get(key);
		if(null == value)
		{
			return null;
		}
		Date parse = null;
		try
		{
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				parse = formatter.parse(value);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		return parse;
	}
	
	/**
	 * 转换成JSONObject
	 * 
	 * @return
	 */
	public JSONObject toJSONObject()
	{
		JSONObject json = new JSONObject();
		Iterator<String> it = this.keySet().iterator();
		while(it.hasNext())
		{
			String key = it.next();
			json.put(key, this.get(key));
		}
		return json;
	}
	
	/**
	 * 转成JSONObject 格式的string
	 */
	public String toJSONString()
	{
		return this.toJSONObject().toString();
	}

	public static Property toProperty(JSONObject json)
	{
		Property ret = new Property();
		Iterator<?> it = json.entrySet().iterator();
		while(it.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry en = (Map.Entry)it.next();
			String key = en.getKey().toString();
			String value = en.getValue().toString();
			ret.put(key, value);
		}
		return ret;
	}

	public static Property MaptoProperty(Map<String,String> map)
	{
		Property ret = new Property();
		Iterator<?> it = map.entrySet().iterator();
		while(it.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry en = (Map.Entry)it.next();
			String key = en.getKey().toString();
			String value = en.getValue().toString();
			ret.put(key, value);
		}
		return ret;
	}
	
	public static void main(String[] args)
	{
		long stat = System.currentTimeMillis();
		for(int i = 0; i < 10000; i++)
		{
			JSONObject p1 = new JSONObject();
			p1.put("name", "ffss");
			p1.put("key", "afdc");
			p1.put("ps", "34");
			Property.toProperty(p1);
		}
		
		System.out.println(System.currentTimeMillis() - stat);
	}
}
