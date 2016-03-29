package com.jsonlib;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonHelper
{
	public static Map<Integer, Integer> Json2MapInt(JSONObject json)
		throws Exception
	{
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(Object o : json.keySet())
		{
			String k = (String)o;
			int v = json.getInt(k);
			int i = Integer.parseInt(k);
			map.put(i, v);
		}
		return map;
	}
	
	/**
	 * 把JSON格式 转成 Map,String
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String,String> Json2MapString(JSONObject json)
	{
        Map<String,String> p = new HashMap<String,String>();
		for(Object key : json.keySet())
		{
			p.put(key.toString(), json.getString(key.toString()));
		}
		return p;
	}
	
	/**
	 * 把JSON格式转成Map<String,Map>
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String,Map<String,String>> Json2MapMap(JSONObject json)
	{

        Map<String,Map<String,String>> mp = new HashMap<String,Map<String,String>>();
		for(Object key : json.keySet())
		{
            JSONObject obj=JSONObject.fromObject(json.get(key));
			mp.put(key.toString(), Json2MapString(obj));
		}
		return mp;
	}

	
	/**
	 * 把JSONArray格式 转成Map<String,Map>
	 * @param jsarry
	 * @param columKey
	 * @return
	 */
	public static Map<String,Map<String,String>> JsonArray2MapMap(JSONArray jsarry,String columKey)
	{
        Map<String,Map<String,String>> mp = new HashMap<String,Map<String,String>>();
		if(jsarry==null){
			return mp;
		}
		for(Object jsonObj : jsarry)
		{
			JSONObject json = (JSONObject)jsonObj;
			mp.put(json.getString(columKey), Json2MapString(json));
		}
		return mp;
	}

    public static JSONObject merge(JSONObject json1, JSONObject json2)
            throws Exception
    {
        JSONObject json = new JSONObject();
        for(Object obj : json1.keySet())
            json.put(obj, json1.get(obj));

        for(Object obj : json2.keySet())
            json.put(obj, json2.get(obj));

        return json;
    }
}
