package com.pseudocode.netflix.eureka.client.discovery.shared.resolver;

//Eureka 服务端点
public interface EurekaEndpoint extends Comparable<Object> {

    //完整的服务URL
    String getServiceUrl();

    @Deprecated
    String getHostName();

    //网络地址
    String getNetworkAddress();

    //端口
    int getPort();

    //是否安全( https )
    boolean isSecure();

    //相对路径
    String getRelativeUri();

}
