package com.httpclient.util;
import org.apache.http.HttpHost;
import com.util.RunTimeUtil;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * httpclient4.3.5工具类
 * httpclient4使用 ReentrantLock（默认非公平） + Condition（每个线程一个）
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**连接超时时间*/
    public static final int CONNECTION_TIMEOUT_MS = 10*1000;//10秒

    /**读取数据超时时间*/
    public static final int SO_TIMEOUT_MS = 10*1000;

    /**
     * httpclient读取内容时使用的字符集
     */
    public static final String UTF8 = "UTF-8";

    public static CloseableHttpClient  httpClient;

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

        RunTimeUtil.addShutdownHook(new Runnable()
        {
            public void run()
            {
                if(httpClient!=null)
                {
                    try {
                        //httpClient在不需要时释放资源
                        httpClient.close();
                    }catch(Exception e)
                    {
                        logger.error("HttpClientUtil shutdownHook httpClient.close() error,",e);
                    }
                }
            }
        });

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

        CloseableHttpResponse response = httpClient.execute(get);

        int rtCode= (response==null)?0:response.getStatusLine().getStatusCode();
        if( HttpStatus.SC_OK!=rtCode)
        {
            logger.info("HttpClientUtil getInvoke rt code="+rtCode+",thread name="+Thread.currentThread().getName());
            get.abort();
            //如果程序逻辑是返回码是非200而抛错,必须关闭连接后再抛错,通过get.abort()能够关闭连接。
            //HttpClient4使用InputStream.close()来确认连接关闭，如果非200的连接直接抛错而莫有关闭此链接，此连接将永远僵死在连接池里头，
            //CLOST_WAIT数目将递增直到最大数MaxPerRouteCount，那时对一个路由的连接已经完全被僵死连接占满。
            //如果在请求中出现异常而没有关闭连接以及上面的逻辑非200莫关闭连接就直接抛错退出方法都可能导致连接池卡住问题
            //就是在连接全部僵死后，后续的请求将一致卡在那里而莫有响应。
            throw new IOException("服务器响应状态异常,失败.rtCode="+rtCode);
        }

        HttpEntity entity = response.getEntity();
        String returnStr=null;
        if (entity != null) {
            returnStr = EntityUtils.toString(entity, charset);
        }
        EntityUtils.consume(entity);
        response.close();
        return returnStr;
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

        CloseableHttpResponse response = httpClient.execute(postMethod);

        int rtCode= (response==null)?0:response.getStatusLine().getStatusCode();
        if( HttpStatus.SC_OK!=rtCode)
        {
            logger.info("HttpClientUtil postInvoke rt code="+rtCode+",thread name="+Thread.currentThread().getName());
            postMethod.abort();
            throw new IOException("服务器响应状态异常,失败.rtCode="+rtCode);
        }

        HttpEntity entity = response.getEntity();
        String returnStr=null;
        if (entity != null) {
            returnStr = EntityUtils.toString(entity, charset);
        }
        EntityUtils.consume(entity);
        response.close();
        return returnStr;
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
        // .setProxy(new HttpHost("127.0.0.1",222)) 设置代理
                .setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
        return requestConfig;
    }

    //只是个设置代理的简单例子
    public void setProxy() {
        httpClient = HttpClients.custom().setProxy(new HttpHost("127.0.0.1",222)).build();
    }
}
