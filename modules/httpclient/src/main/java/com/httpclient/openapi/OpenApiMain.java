package com.httpclient.openapi;

import com.alibaba.fastjson.JSON;
import com.httpclient.openapi.utils.Response;
import com.httpclient.util.HttpClientUtil2;

import java.util.HashMap;
import java.util.Map;

public class OpenApiMain {

    public static void main(String [] args ) throws Exception {

        String openApiUrl = "";
        String openApiPath = "";
        String openApiKey="";
        String openApiSecret="";

        Map<String, String> requestHeader = new HashMap<String, String>();
        requestHeader.put("X-User-Group","1");

        String bizAccessToken = "";
        String bizAppKey = "";
        String bizMethod = "";
        String bizTimestamp = "";
        String bizSign = "";

        Map<String, Object> strParams = new HashMap<String, Object>();
        strParams.put("oid",0L);
        String bizParam=JSON.toJSONString(strParams);

        Map<String, String> bizParams = new HashMap<String, String>();
        bizParams.put("appkey", bizAppKey);
        bizParams.put("access_token", bizAccessToken);
        bizParams.put("param",bizParam);
        bizParams.put("method", bizMethod);
        bizParams.put("sign", bizSign);
        bizParams.put("timestamp", bizTimestamp);
        bizParams.put("version", "1");
        bizParams.put("signMethod", "MD5");

        String params = getAPIParams(bizParams);
        String realUrl ="";
        String response = HttpClientUtil2.post(realUrl, params);
        System.out.println(response);

        Response openApiResponse = OpenApiSdk.proxyFormUrl(openApiUrl,openApiPath,openApiKey, openApiSecret,
                bizTimestamp, "2.0", bizParams, requestHeader);
        System.out.println(JSON.toJSONString(openApiResponse));
    }

    public static  String getAPIParams( Map<String, String> map) {
        String result ="";
        for(String key : map.keySet()) {
            result +=key+"="+map.get(key)+"&";
        }
        if(result.endsWith("&")) {
            result = result.substring(0,result.length()-1);
        }
        return result;
    }

}
