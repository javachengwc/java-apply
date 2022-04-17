package com.httpclient.openapi.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpProxy {

    public static Response proxyFormUrl(String url, Map<String, String> map, Map<String, String> requestHeader, String contentType) throws Exception {
        List<NameValuePair> list = new ArrayList();
        Iterator var5 = map.keySet().iterator();

        while(true) {
            while(var5.hasNext()) {
                String key = (String)var5.next();
                Object value = map.get(key);
                NameValuePair pair = null;
                if (value instanceof String) {
                    pair = new NameValuePair(key, value.toString());
                    list.add(pair);
                } else if (value instanceof String[]) {
                    String[] values = (String[])((String[])value);

                    for(int i = 0; i < values.length; ++i) {
                        list.add(new NameValuePair(key, values[i]));
                    }
                }
            }

            PostMethod postMethod = new PostMethod(url);
            postMethod.setRequestBody((NameValuePair[])list.toArray(new NameValuePair[list.size()]));
//            String params = getAPIParams(map);
//            System.out.println("params="+params);
//            postMethod.setRequestEntity( new StringRequestEntity(params,contentType, "UTF-8"));
            return postProxy(postMethod, requestHeader, contentType);
        }
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

    public static Response proxyJson(String url, JSONObject json, Map<String, String> requestHeader, String contentType) throws UnsupportedEncodingException {
        PostMethod postMethod = new PostMethod(url);
        RequestEntity rs = new StringRequestEntity(json.toJSONString(), contentType, "UTF-8");
        postMethod.setRequestEntity(rs);
        return postProxy(postMethod, requestHeader, contentType);
    }

    public static Response proxyApplicationJson(String url, String jsonString, Map<String, String> requestHeader, String contentType) throws UnsupportedEncodingException {
        PostMethod postMethod = new PostMethod(url);
        RequestEntity rs = new StringRequestEntity(jsonString, contentType, "UTF-8");
        postMethod.setRequestEntity(rs);
        return postProxy(postMethod, requestHeader, contentType);
    }

    private static Response postProxy(PostMethod postMethod, Map<String, String> requestHeader, String contentType) {
        Response response = null;
        InputStream inputStream = null;
        GetMethod redirect = null;
        StringBuilder body = null;

        try {
            response = new Response();
            postMethod.setRequestHeader("User-Agent", "");
            postMethod.setRequestHeader("Content-Encoding", "text/html");
            postMethod.setRequestHeader("Content-Type", contentType + ";charset=UTF-8");
            postMethod.setRequestHeader("Connection", "close");
            postMethod.setRequestHeader("X-Hunter-TraceId", UUID.randomUUID().toString().replace("-", ""));
            postMethod.setRequestHeader("X-Hunter-WorkId", "1,2");
            postMethod.setRequestHeader("X-Hunter-URI", "");
            if (!MapUtils.isEmpty(requestHeader)) {
                Iterator var7 = requestHeader.entrySet().iterator();

                while(var7.hasNext()) {
                    Map.Entry<String, String> item = (Map.Entry)var7.next();
                    postMethod.setRequestHeader((String)item.getKey(), (String)item.getValue());
                }
            }

            AtomicInteger responseCode = new AtomicInteger(0);
            HttpClient httpClient = new HttpClient();
            responseCode.set(httpClient.executeMethod(postMethod));
            if (responseCode.get() != 301 && responseCode.get() != 302) {
                inputStream = postMethod.getResponseBodyAsStream();
                body = getResult(inputStream, "UTF-8");
            } else {
                Header locationHeader = postMethod.getResponseHeader("location");
                if (locationHeader == null) {
                    throw new HttpException("ERROR:---http request error");
                }

                String location = locationHeader.getValue();
                redirect = new GetMethod(location);
                responseCode.set(httpClient.executeMethod(redirect));
                if (responseCode.get() == 301 || responseCode.get() == 302) {
                    throw new HttpException("ERROR:---http request redirect, http status is " + responseCode.get());
                }

                inputStream = redirect.getResponseBodyAsStream();
                body = getResult(inputStream, "UTF-8");
            }

            response.setStatus(responseCode.get());
            response.setBody(body);
        } catch (HttpException var23) {
            var23.printStackTrace();
        } catch (IOException var24) {
            var24.printStackTrace();
        } catch (JSONException var25) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException var22) {
                    var22.printStackTrace();
                }
            }

            if (postMethod != null) {
                postMethod.releaseConnection();
            }

            if (redirect != null) {
                redirect.releaseConnection();
            }

        }

        return response;
    }

    private static StringBuilder getResult(InputStream inputStream, String code) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, code));
        StringBuilder result = new StringBuilder();

        String line;
        while((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result;
    }
}
