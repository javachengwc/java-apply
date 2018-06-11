package com.util.http;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class UrlUtil {
	
public static String paramPatternString ="[\\?|&]param=([^\\?&]*)";
	
	public static String getQueryString(String url)
	{
		int i = url.indexOf("?");
		if(i<0)
		{
			return "";
		}
		return url.substring(i+1);
	}
	
	public static String getDomain(String url)
	{
		
		String u =url;
		if(u.startsWith("http://"))
		{
			u =u.substring(7);
		}
		if(u.startsWith("https://"))
		{
			u =u.substring(8);
		}
		int i = u.indexOf(":");
		if(i>=0)
		{
			return u.substring(0,i);
		}
		int j = u.indexOf("/");
		if(j<0)
		{
			return u;
		}
		return u.substring(0,j);
	}
	
	public static int getPort(String url)
	{
		String u =url;
		if(u.startsWith("http://"))
		{
			u =u.substring(7);
		}
		if(u.startsWith("https://"))
		{
			u =u.substring(8);
		}
		int i = u.indexOf(":");
		if(i<0)
		{
			return 80;
		}
		u = u.substring(i+1);
		int j = u.indexOf("/");
		if(j<0)
		{
			return Integer.parseInt(u);
		}
		return Integer.parseInt(u.substring(0,j));
	}

    public static String getPath(String url)
    {
        String u =url;
        if(u.startsWith("http://"))
        {
            u =u.substring(7);
        }
        if(u.startsWith("https://"))
        {
            u =u.substring(8);
        }
        int i = u.indexOf("/");
        if(i>=0)
        {
            u= u.substring(i);
            i= u.indexOf("?");
            if(i>0)
            {
                u=u.substring(0,i);
            }
            return u;
        }
        return "";
    }
	
	public static Map<String,String> getParamMap(String url)
	{
		Map<String,String> map = new HashMap<String,String>();
		String queryStr=getQueryString(url);
		if(StringUtils.isBlank(queryStr))
		{
			return map;
		}
		String pers [] = queryStr.split("\\&");
		for(String p:pers)
		{
			if(StringUtils.isBlank(p))
			{
				continue;
			}
			String as [] = p.split("=");
			int len = as.length;
            String val=(len>1)?as[1]:"";
            if (StringUtils.isNotBlank(val)) {
                val = val.replaceAll("#.*", "");
            }
			map.put(as[0], val);
			
		}
		return map;
	}
	
	public static String getParam(String url,String param)
	{
		 Map<String,String> map = getParamMap(url);
		 if(map==null)
		 {
			 return null;
		 }
		 return map.get(param);
	}

	public static String getParamByPattern(String url,String param)
	{
		String patStr = paramPatternString.replace("param", param);
		Pattern paramPattern=Pattern.compile(patStr);
		Matcher ma= paramPattern.matcher(url);
		if(ma.find())
		{
			//System.out.println(ma.group());
		    String val= ma.group(1);
            if (StringUtils.isNotBlank(val)) {
                val = val.replaceAll("#.*", "");
            }
            return val;
		}
		return "";
	}
	
	public static void main(String args [] )
	{
	
		String url ="https://www.cc.com/haha?aa=bb&cc=dd&ee=&password=aa&ak=ak#aabbcc";
		System.out.println(getPort(url));
		System.out.println(getDomain(url));
        System.out.println(getPath(url));
		
		System.out.println(getParamByPattern(url,"aa"));
		System.out.println(getParamByPattern(url,"cc"));
		System.out.println(getParamByPattern(url,"ee"));
		System.out.println(getParamByPattern(url,"password"));
        System.out.println(getParamByPattern(url,"ak"));
        System.out.println(getParam(url,"ak"));
	}

}
