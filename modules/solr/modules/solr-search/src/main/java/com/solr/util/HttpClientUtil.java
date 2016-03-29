package com.solr.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.EncodingUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Send httpClient request
 */
public class HttpClientUtil {
	//send httpClient
    public static String sendHttpClient(String url){
    	HttpClient client = new HttpClient();
    	java.security.Security.setProperty("networkaddress.cache.ttl" , "0");
		GetMethod getMethod = new GetMethod(url);
		getMethod.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
		getMethod.addRequestHeader("Connection", "close");  
		getMethod.addRequestHeader("max-age","0");
		if(url.indexOf("?") != -1){
			String s = url.substring(url.indexOf("?") + 1);
			String[] s1 = s.split("&");
	        if (s1 != null) {
	            NameValuePair [] pairs = new NameValuePair[s1.length+1];
	            for (int i = 0; i < s1.length; i++) {
	                String name = s1[i].substring(0,s1[i].indexOf("="));
	                String value= s1[i].substring(s1[i].indexOf("=")+1);
	                pairs [i]= new NameValuePair(name,value);
	            }
	            pairs[s1.length] = new NameValuePair("time",String.valueOf(new Date().getTime()));
	            getMethod.setQueryString(EncodingUtil.formUrlEncode(pairs, "utf8"));
	        }
		}else{
			NameValuePair [] pairs = new NameValuePair[1];
			pairs[0] = new NameValuePair("time",String.valueOf(new Date().getTime()));
			getMethod.setQueryString(EncodingUtil.formUrlEncode(pairs, "utf8"));
		}
		InputStream resStream = null;
		try {
			client.executeMethod(getMethod);
			resStream = getMethod.getResponseBodyAsStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(resStream,"utf-8"));
	        StringBuffer resBuffer = new StringBuffer();
	        String resTemp = "";
	        while((resTemp = br.readLine()) != null){
	            resBuffer.append(resTemp);  
	        }
	        String response = resBuffer.toString();
			return response;
			
//			return getMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(resStream != null){
				try {
					resStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//最后关闭连接
			getMethod.releaseConnection();
		}
		return null;
    }
    
    public static String sendPostHttpClient(String url,Map<String,String> params){
    	HttpClient client = new HttpClient();
		PostMethod pm = new PostMethod(url);
		pm.addRequestHeader("Connection", "close");
		Set<String> keySet = params.keySet();
		if(params != null){
			for(String key:keySet){
				pm.setParameter(key, params.get(key));
			}
		}
		InputStream resStream = null;
    	try {
			client.executeMethod(pm);
			resStream = pm.getResponseBodyAsStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(resStream,"utf-8"));
	        StringBuffer resBuffer = new StringBuffer();
	        String resTemp = "";
	        while((resTemp = br.readLine()) != null){
	            resBuffer.append(resTemp);  
	        }
	        String response = resBuffer.toString();
			return response;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(resStream != null){
				try {
					resStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//最后关闭连接
			pm.releaseConnection();
		}
		return null;
    }
}
