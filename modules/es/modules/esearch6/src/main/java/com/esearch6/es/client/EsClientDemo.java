package com.esearch6.es.client;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.sniff.SniffOnFailureListener;
import org.elasticsearch.client.sniff.Sniffer;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;

/**
 * Elasticsearch java client有TransportClient，JestClient，RestClient，2.3版本中还有NodeClient
 * springboot中集成es的是spring-data-elasticsearch
 * RestClient对象是线程安全的，理论上跟使用它的应用程序有相同的生命周期
 * es官方客户端有两种：restclient和transprortclient，
 * 前者是基于restful的，直接操作各种restful api和query dsl，比较简单，没有orm功能；
 * 后者是基于java api封装的orm框架，封装比较死板，不太灵活，兼容性差，不能像调用restful那样直接操作query dsl，也不能直接针对query dsl，
 * 在head插件或者kibana里面调试和调优query dsl，写出了query dsl还要想方设法转换成对应的java api的调用方式。
 */
public class EsClientDemo {

    public RestClient buildClient1() {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")).build();
        return restClient;
    }

    public RestClient buildClient2() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};
        builder.setDefaultHeaders(defaultHeaders);  //设置请求头
        builder.setMaxRetryTimeoutMillis(60000);    //最大重试超时时长，默认是30秒，和socket默认的超时时间是一样的
        builder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(HttpHost host) {
                // TODO:监听探测节点，当节点失败时，这里处理，需要开启失败探测功能
            }
        });
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {

            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                //设置一个callback来修改默认的请求配置
                requestConfigBuilder.setSocketTimeout(10000);
                requestConfigBuilder.setSocketTimeout(60000);
                requestConfigBuilder.setProxy(new HttpHost("proxy", 9000, "http"));
                return requestConfigBuilder;
            }
        });

        return builder.build();
    }

    public RestClient buildClient3() {

        //基础身份验证
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("user", "password"));

        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });
        return builder.build();
    }

    public RestClient buildClient5() {

        //https加密通信
        Path keyStorePath=null;
        String keyStorePass="";
        try {
            KeyStore truststore = KeyStore.getInstance("jks");
            InputStream is = Files.newInputStream(keyStorePath);
            truststore.load(is, keyStorePass.toCharArray());

            SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(truststore, null);
            final SSLContext sslContext = sslBuilder.build();
            RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "https"))
                    .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                        @Override
                        public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                            return httpClientBuilder.setSSLContext(sslContext);
                        }
                    });
            return builder.build();
        }catch(Exception e) {

        }
        return null;
    }

    public RestClient buildClient6() {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http"))
                .build();
        //嗅探器
        //Sniffer将利用关联的RestClient周期性（默认每5分钟）的获取集群的节点列表并且通过调用RestClient#setHosts进行更新
        //Sniffer默认每5分钟更新一次节点信息
        Sniffer sniffer = Sniffer.builder(restClient).build();
//        自定义sniffer的更新周期
//        Sniffer sniffer = Sniffer.builder(restClient)
//                .setSniffIntervalMillis(60000).build();
        return restClient;

        //Sniffer对象跟RestClient有相同的生命周期，并且在关闭时要先关闭Sniffer，然后再关闭RestClient
//        sniffer.close();
//        restClient.close();
    }

    public RestClient buildClient7() {

        //每当出现错误时会立即更新节点列表而不是等待下次更新周期
        SniffOnFailureListener sniffOnFailureListener = new SniffOnFailureListener();
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200))
                .setFailureListener(sniffOnFailureListener)
                .build();
        Sniffer sniffer = Sniffer.builder(restClient)
                .setSniffAfterFailureDelayMillis(30000)
                .build();
        sniffOnFailureListener.setSniffer(sniffer);
        return restClient;
    }
}
