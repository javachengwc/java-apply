package com.pseudocode.netflix.feign.core;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

public final class Request
{
    private final String method;
    private final String url;
    private final Map<String, Collection<String>> headers;
    private final byte[] body;
    private final Charset charset;

    public static Request create(String method, String url, Map<String, Collection<String>> headers, byte[] body, Charset charset)
    {
        return new Request(method, url, headers, body, charset);
    }

    Request(String method, String url, Map<String, Collection<String>> headers, byte[] body, Charset charset)
    {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.charset = charset;
    }

    public String method()
    {
        return this.method;
    }

    public String url()
    {
        return this.url;
    }

    public Map<String, Collection<String>> headers()
    {
        return this.headers;
    }

    public Charset charset()
    {
        return this.charset;
    }

    public byte[] body()
    {
        return this.body;
    }

    //超时时间，默认连接超时10s，读超时60s
//    使用url方式：必须通过这个参数来设置，才生效
//    @Configuration
//    public class XxxConfig {
//        @Bean
//        public Request.Options options(){
//            Request.Options o = new Options(1000, 1000);
//            return o;
//        }
//    }
//注意此类不能被spring容器扫描到，否则会对全局生效
//@FeignClient(url="xxx",configuration= {XxxConfig.class})
//使用name方式：此时已经集成了ribbon，可以使用以下配置来设置，如果此时也配置了Options，以下配置会被覆盖
//    设置后对所有的feignclient生效
//    feign.client.config.default.readTimeout=10000
//    feign.client.config.default.ConnectTimeout=10000  或
//    对所有的feignclient生效
//    ribbon.ReadTimeout=10000
//    ribbon.ConnectTimeout=2000
//    对指定的feignclien生效
//    [feignclientName].ribbon.ReadTimeout=10000
//    [feignclientName].ribbon.ConnectTimeout=2000
//如果开启Hytrix，hytrix也有超时时间设置，但是hytrix是封装在feign基础之上的
//hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=10000  默认:1000毫秒
//hystrix.command.default.execution.timeout.enabled=false           //关闭hytrix的超时时间
    public static class Options {
        private final int connectTimeoutMillis;
        private final int readTimeoutMillis;

        public Options(int connectTimeoutMillis, int readTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            this.readTimeoutMillis = readTimeoutMillis;
        }

        public Options() {
            this(10000, 60000);
        }

        public int connectTimeoutMillis()
        {
            return this.connectTimeoutMillis;
        }

        public int readTimeoutMillis()
        {
            return this.readTimeoutMillis;
        }
    }
}
