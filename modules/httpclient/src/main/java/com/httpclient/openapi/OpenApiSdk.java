package com.httpclient.openapi;

import com.alibaba.fastjson.JSONObject;
import com.httpclient.openapi.utils.CommonContents;
import com.httpclient.openapi.utils.HttpProxy;
import com.httpclient.openapi.utils.Response;
import com.httpclient.openapi.utils.Signs;

import java.util.HashMap;
import java.util.Map;

public class OpenApiSdk {

    public static Response proxyFormUrl(String openApiUrl, String uri, String app_key, String appSecret, String timestamp, String version, Map<String, String> params, Map<String, String> requestHeader) throws Exception {
        String token = getToken(app_key, appSecret, timestamp, version, params);
        if (requestHeader == null) {
            requestHeader = new HashMap();
        }

        ((Map)requestHeader).put("Access-Key", app_key);
        ((Map)requestHeader).put("Access-Token", token);
        ((Map)requestHeader).put("Access-Timestamp", timestamp);
        ((Map)requestHeader).put("Access-Version", version);
        return HttpProxy.proxyFormUrl(openApiUrl + uri, params, (Map)requestHeader, CommonContents.FORMUrl);
    }

    public static Response proxyJson(String openApiUrl, String uri, String app_key, String appSecret, String timestamp, String version, JSONObject params, Map<String, String> requestHeader) throws Exception {
        String token = getToken(app_key, appSecret, timestamp, version, (Map)null);
        if (requestHeader == null) {
            requestHeader = new HashMap();
        }

        ((Map)requestHeader).put("Access-Key", app_key);
        ((Map)requestHeader).put("Access-Token", token);
        ((Map)requestHeader).put("Access-Timestamp", timestamp);
        ((Map)requestHeader).put("Access-Version", version);
        return HttpProxy.proxyJson(openApiUrl + uri, params, (Map)requestHeader, CommonContents.JSON);
    }

    public static Response proxyApplicationJson(String openApiUrl, String uri, String app_key, String appSecret, String timestamp, String version, String jsonString, Map<String, String> requestHeader) throws Exception {
        String token = getToken(app_key, appSecret, timestamp, version, (Map)null);
        if (requestHeader == null) {
            requestHeader = new HashMap();
        }

        ((Map)requestHeader).put("Access-Key", app_key);
        ((Map)requestHeader).put("Access-Token", token);
        ((Map)requestHeader).put("Access-Timestamp", timestamp);
        ((Map)requestHeader).put("Access-Version", version);
        return HttpProxy.proxyApplicationJson(openApiUrl + uri, jsonString, (Map)requestHeader, CommonContents.JSON);
    }

    public static String getToken(String app_key, String appSecret, String timestamp, String version, Map<String, String> params) throws Exception {
        Map<String, Object> tockenParams = new HashMap();
        if (params != null) {
            tockenParams.putAll(params);
        }

        tockenParams.put("app_key", app_key);
        tockenParams.put("timestamp", timestamp);
        tockenParams.put("v", version);
        String token = Signs.sign(appSecret, tockenParams);
        return token;
    }
}
