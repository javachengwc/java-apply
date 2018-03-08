package com.solr7.index.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {


    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static final int CONNECTION_TIMEOUT_MS = 2*1000;//2秒

    public static final int SO_TIMEOUT_MS = 5*1000;

    public static final String UTF8 = "UTF-8";

    public static CloseableHttpClient httpClient;

    static {
        CloseableHttpClient client=null;
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(50);
        manager.setDefaultMaxPerRoute(20);
        client = HttpClientBuilder.create().setConnectionManager(manager).build();
        httpClient=client;
    }

    public static RequestConfig genRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
        return requestConfig;
    }

    public static String get(String url, Map<String, String> params) throws IOException, URISyntaxException {
        return get(url, params, UTF8);
    }

    public static String get(String url, Map<String, String> params, String charset) throws IOException, URISyntaxException {

        HttpGet get = genHttpGet(url, params);
        get.setConfig(genRequestConfig());
        String returnStr=null;
        try{
            CloseableHttpResponse response = httpClient.execute(get);
            int rtCode= (response==null)?0:response.getStatusLine().getStatusCode();
            if( HttpStatus.SC_OK!=rtCode)
            {
                logger.info("HttpClientUtil get rt code="+rtCode+",thread name="+Thread.currentThread().getName());
                get.abort();
                throw new IOException("服务器响应状态异常 code="+rtCode);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                returnStr = EntityUtils.toString(entity, charset);
            }
            EntityUtils.consume(entity);
            response.close();
        } catch (Exception e) {
            logger.error("HttpClientUtil get error,thread name="+Thread.currentThread().getName(),e);
            get.abort();
            throw new RuntimeException("服务调用异常");
        }
        return returnStr;
    }

    public static HttpGet genHttpGet(String url, Map<String, String> params) throws URISyntaxException {
        HttpGet get = new HttpGet(genGetUrl(url, params));
        return get;
    }

    private static String genGetUrl(String url, Map<String, String> params) {

        StringBuffer uriStr = new StringBuffer(url);
        if (params != null) {
            List<NameValuePair> ps = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                ps.add(new BasicNameValuePair(key, params.get(key)));
            }
            uriStr.append("?");
            uriStr.append(URLEncodedUtils.format(ps, UTF8));
        }
        return uriStr.toString();
    }

    public static String post(String url,String bodyJsonStr) throws URISyntaxException, IOException {
        return post(url, bodyJsonStr, UTF8);
    }

    public static String post(String url, String bodyJsonStr, String charset) throws URISyntaxException , IOException {

        HttpPost postMethod = genHttpPost(url, bodyJsonStr);
        postMethod.setConfig(genRequestConfig());
        String returnStr=null;
        try{
            CloseableHttpResponse response = httpClient.execute(postMethod);
            int rtCode= (response==null)?0:response.getStatusLine().getStatusCode();
            if( HttpStatus.SC_OK!=rtCode)
            {
                logger.info("HttpClientUtil post rt code="+rtCode+",thread name="+Thread.currentThread().getName());
                postMethod.abort();
                throw new IOException("服务器响应状态异常 code="+rtCode);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                returnStr = EntityUtils.toString(entity, charset);
            }
            EntityUtils.consume(entity);
            response.close();
        } catch (Exception e) {
            logger.error("HttpClientUtil post error,thread name="+Thread.currentThread().getName(),e);
            postMethod.abort();
            throw new RuntimeException("服务调用异常");
        }
        return returnStr;
    }

    public static HttpPost genHttpPost(String url, String bodyJsonStr) throws UnsupportedEncodingException, URISyntaxException {
        HttpPost post = new HttpPost(url);
        post.addHeader(HTTP.CONTENT_TYPE,"application/json; charset=utf-8");
        post.setHeader("Accept", "application/json");
        post.setEntity(new StringEntity(bodyJsonStr, Charset.forName(UTF8)));
        return post;
    }
}
