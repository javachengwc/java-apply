package com.rule.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * httpclient3.1多线程客户端
 */
public class GeneralHttpExecutor {

    public static final Integer MAX_IDLE_TIME_OUT = 60000;
    public static HttpClient httpClient = null;

    static {
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setConnectionTimeout(20*1000);
        params.setSoTimeout(30*1000);
        // 最大连接数,多少线程密集调用，perhost就设置多少线程 比较合适
        params.setMaxTotalConnections(20);
        params.setDefaultMaxConnectionsPerHost(10);

        connectionManager.setParams(params);
        connectionManager.closeIdleConnections(MAX_IDLE_TIME_OUT);

        //HttpClientParams httpClientParams = new HttpClientParams();
        // 设置httpClient的连接超时，对连接管理器设置的连接超时是无用的
        //httpClientParams.setConnectionManagerTimeout(5000);

        httpClient = new HttpClient(connectionManager);
        //httpClient.setParams(httpClientParams);
    }

    public static String get(String url) throws HttpException, IOException {
        GetMethod get = new GetMethod(url);
        get.addRequestHeader("Content-type" , "text/html; charset=utf-8");
        try {
            httpClient.executeMethod(get);
            InputStream stream =get.getResponseBodyAsStream();
            return streamToString(stream, Charset.forName("UTF-8"));
        } finally {
            get.releaseConnection();
        }
    }

    public static String post(String url, String content) throws IOException {
        return post(url, content, "text/json", "utf-8");
    }

    public static String post(String url, String content, String contentType, String charset) throws IOException {
        PostMethod post = new PostMethod(url);
        post.setRequestEntity(new StringRequestEntity(content, contentType, charset));
        try {
            httpClient.executeMethod(post);
            InputStream stream =post.getResponseBodyAsStream();
            return streamToString(stream,Charset.forName("UTF-8"));
        } finally {
            post.releaseConnection();
        }
    }

    public static String streamToString(InputStream stream, Charset charset) throws IOException{
        BufferedReader reader=new BufferedReader(new InputStreamReader(stream, charset));
        StringBuilder respBuilder=new StringBuilder();
        String respLine;
        while ((respLine=reader.readLine())!=null) {
            respBuilder.append(respLine);
        }
        return respBuilder.toString();
    }
}
