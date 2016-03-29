package com.httpclient.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * httpclient4.3.5工具类
 */
public class HttpClientUtil {

    /**连接超时时间*/
    public static final int CONNECTION_TIMEOUT_MS = 10*1000;//10秒

    /**读取数据超时时间*/
    public static final int SO_TIMEOUT_MS = 10*1000;

    /**
     * httpclient读取内容时使用的字符集
     */
    public static final String UTF8 = "UTF-8";

    public static HttpClient httpClient;

    static {

        //创建HttpClient

        CloseableHttpClient client=null;

        PoolingHttpClientConnectionManager  manager = new PoolingHttpClientConnectionManager();
        // 将最大连接数增加到50
        manager.setMaxTotal(50);
        // 将每个路由基础的连接增加到20
        manager.setDefaultMaxPerRoute(20);

        client = HttpClientBuilder.create()
                .setConnectionManager(manager)
                .build();

        httpClient=client;
    }

    /**
     * 简单get调用
     * @param url
     * @param params
     */
    public static String getInvoke(String url, Map<String, String> params) throws IOException, URISyntaxException {

        return getInvoke(url, params, UTF8);
    }

    /**
     * 简单get调用
     * @param url
     * @param params
     */
    public static String getInvoke(String url, Map<String, String> params, String charset) throws IOException, URISyntaxException {

        HttpGet get = buildHttpGet(url, params);
        get.setConfig(buildRequestConfig());

        HttpResponse response = httpClient.execute(get);

        assertStatus(response);

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String returnStr = EntityUtils.toString(entity, charset);
            return returnStr;
        }
        return null;
    }

    /**
     * 简单post调用
     * @param url
     * @param params
     */
    public static String postInvoke(String url, Map<String, String> params) throws URISyntaxException, IOException {

        return postInvoke(url, params, UTF8);
    }

    /**
     * 简单post调用
     * @param url
     * @param params
     */
    public static String postInvoke(String url, Map<String, String> params, String charset) throws URISyntaxException , IOException {

        HttpPost postMethod = buildHttpPost(url, params);
        postMethod.setConfig(buildRequestConfig());

        HttpResponse response = httpClient.execute(postMethod);

        assertStatus(response);

        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String returnStr = EntityUtils.toString(entity, charset);
            return returnStr;
        }

        return null;
    }


    /**
     * 构建httpGet对象
     */
    public static HttpGet buildHttpGet(String url, Map<String, String> params) throws URISyntaxException {

        HttpGet get = new HttpGet(buildGetUrl(url, params));

        return get;
    }

    /**
     * build getUrl str
     */
    private static String buildGetUrl(String url, Map<String, String> params) {

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

    /**
     * 构建httpPost对象
     */
    public static HttpPost buildHttpPost(String url, Map<String, String> params) throws UnsupportedEncodingException, URISyntaxException {

        HttpPost post = new HttpPost(url);
        post.setHeader(HTTP.CONTENT_ENCODING, UTF8);// setting

        HttpEntity he = null;
        if (params != null) {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                formparams.add(new BasicNameValuePair(key, params.get(key)));
            }
            he = new UrlEncodedFormEntity(formparams, UTF8);
            post.setEntity(he);
        }
        return post;
    }

    /**
     * 构建公用RequestConfig
     */
    public static RequestConfig buildRequestConfig() {

        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SO_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
        return requestConfig;
    }

    /**
     * 强验证必须是200状态否则报异常
     */
    static void assertStatus(HttpResponse res) throws IOException {
        switch (res.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_OK:
                break;
            default:
                throw new IOException("服务器响应状态异常,失败.");
        }
    }
}
