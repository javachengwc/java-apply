package com.httpclient.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HttpClientUtil2 {

    private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil2.class);

    private static Lock lock = new ReentrantLock();

    private static Map<String, PerRoute> perRoutes = null;

    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;

    private static CloseableHttpClient httpClient;


    static {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null,
                    new TrustSelfSignedStrategy())
                    .build();
        } catch (Exception e) {
            logger.error("sslcontext error",e);
        }

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslsf)
                .build();

        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> httpConnectionFactory =
                new ManagedHttpClientConnectionFactory(DefaultHttpRequestWriterFactory.INSTANCE,
                        DefaultHttpResponseParserFactory.INSTANCE);

        DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry,
                httpConnectionFactory, dnsResolver);
        poolingHttpClientConnectionManager.setMaxTotal(400);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(200);

        SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
        poolingHttpClientConnectionManager.setDefaultSocketConfig(defaultSocketConfig);
        poolingHttpClientConnectionManager.setValidateAfterInactivity(5 * 10000);

    }

    public static synchronized void initPerRoute(Map<String, PerRoute> perRouteMap) {
        if (perRoutes != null && !perRouteMap.isEmpty()) {
            Iterator<String> iterator = perRouteMap.keySet().iterator();
            while (iterator.hasNext()) {
                PerRoute perRoute = perRouteMap.get(iterator.next());
                if (perRoute.getNumberOfConcurrent() > 0) {
                    String hostname = perRoute.getUrl().split("/")[2];
                    int port = 80;
                    if (hostname.contains(":")) {
                        String[] arr = hostname.split(":");
                        hostname = arr[0];
                        port = Integer.parseInt(arr[1]);
                    }
                    poolingHttpClientConnectionManager.setMaxPerRoute(new HttpRoute(new HttpHost(hostname, port)), perRoute.getNumberOfConcurrent());

                }
            }
        }
        perRoutes = perRouteMap;
    }

    public static String post(String url, Map<String, Object> params) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        setConfig(httpPost, url);
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            response = getHttpClient().execute(httpPost, HttpClientContext.create());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            }else{
                throw new RuntimeException("接口访问失败=>状态码："+response.getStatusLine().getStatusCode()+",msg:"+EntityUtils.toString(response.getEntity()));
            }
        } finally {
            if (response != null) {
                EntityUtils.consume(response.getEntity());
            }
        }
    }

    public static String post(String url, String params) throws IOException {
        return post(url,params,null);
    }

    public static String post(String url, String params,String format) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        setConfig(httpPost, url,format);
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new StringEntity(params, Consts.UTF_8));
            response = getHttpClient().execute(httpPost, HttpClientContext.create());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            }else{
                throw new RuntimeException("接口访问失败=>状态码："+response.getStatusLine().getStatusCode()+",msg:"+EntityUtils.toString(response.getEntity()));
            }
        } finally {
            if (response != null) {
                EntityUtils.consume(response.getEntity());
            }
        }
    }

    public static String get(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        setConfig(httpGet, url);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(httpGet, HttpClientContext.create());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            }else{
                throw new RuntimeException("接口访问失败=>状态码："+response.getStatusLine().getStatusCode()+",msg:"+EntityUtils.toString(response.getEntity()));
            }
        } finally {
            if (response != null) {
                EntityUtils.consume(response.getEntity());
            }
        }
    }


    private static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            try {
                lock.lock();
                if (httpClient == null) {
                    RequestConfig defaultRequestConfig = RequestConfig.custom()
                            .setConnectTimeout(2 * 10000)
                            .setSocketTimeout(5 * 10000)
                            .setConnectionRequestTimeout(20000).build();
                    httpClient = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager)
                            .setConnectionManagerShared(false)
                            .evictIdleConnections(60, TimeUnit.SECONDS)
                            .evictExpiredConnections().setConnectionTimeToLive(60, TimeUnit.SECONDS)
                            .setDefaultRequestConfig(defaultRequestConfig)
                            .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                            .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                            .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();
                    Runtime.getRuntime().addShutdownHook(new Thread() {
                        @Override
                        public void run() {
                            try {
                                if (httpClient != null) {
                                    httpClient.close();
                                }
                            } catch (IOException e) {
                                logger.error("httpClient close error",e);
                            }

                        }
                    });
                }
            } finally {
                lock.unlock();
            }
        }
        return httpClient;
    }

    private static void setConfig(HttpRequestBase httpRequestBase, String url) {
        setConfig(httpRequestBase,url,null);
    }
    private static void setConfig(HttpRequestBase httpRequestBase, String url,String format) {
        // 设置Header等
        // 配置请求的超时设置
        RequestConfig requestConfig = null;
        if (perRoutes != null && perRoutes.containsKey(url)) {
            PerRoute perRoute = perRoutes.get(url);
            if (perRoute.getConnectTimeout() > 0 && perRoute.getRequestTimeout() > 0
                    && perRoute.getSocketTimeout() > 0) {
                requestConfig = RequestConfig.custom()
                        .setConnectTimeout(perRoute.getConnectTimeout())
                        .setSocketTimeout(perRoute.getSocketTimeout())
                        .setConnectionRequestTimeout(perRoute.getRequestTimeout()).build();
            }
        }

        if (requestConfig == null) {
            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(2 * 20000)
                    .setSocketTimeout(5 * 50000)
                    .setConnectionRequestTimeout(2000).build();
        }
        if(StringUtils.equals(format,"json")){
            httpRequestBase.setHeader("Content-type", "application/json; charset=utf-8");
        }else{
            httpRequestBase.setHeader("Charsert", "UTF-8");
            httpRequestBase.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        httpRequestBase.setConfig(requestConfig);
    }


    class PerRoute {
        private String url;
        private int numberOfConcurrent;
        private int requestTimeout;
        private int connectTimeout;
        private int socketTimeout;


        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getNumberOfConcurrent() {
            return numberOfConcurrent;
        }

        public void setNumberOfConcurrent(int numberOfConcurrent) {
            this.numberOfConcurrent = numberOfConcurrent;
        }

        public int getRequestTimeout() {
            return requestTimeout;
        }

        public void setRequestTimeout(int requestTimeout) {
            this.requestTimeout = requestTimeout;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public int getSocketTimeout() {
            return socketTimeout;
        }

        public void setSocketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
        }
    }

}
