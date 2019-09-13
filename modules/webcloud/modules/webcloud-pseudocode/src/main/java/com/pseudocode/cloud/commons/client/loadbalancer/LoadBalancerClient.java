package com.pseudocode.cloud.commons.client.loadbalancer;

import com.pseudocode.cloud.commons.client.ServiceInstance;

import java.io.IOException;
import java.net.URI;

public interface LoadBalancerClient extends ServiceInstanceChooser {

    //使用从负载均衡器中挑选出的服务实例来执行请求
    <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException;

    <T> T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest<T> request) throws IOException;

    /**
     * 为系统构建一个合适的host:port形式的url。
     * 在分布式系统中，使用逻辑上的服务名称作为host来构建URI（替代服务实例的host:port形式）进行请求，比如说myservice/path/to/service
     * 此函数参数ServiceInstance对象是带有host和port的具体服务实例，而后者URI对象则是使用逻辑服务名定义为host的URI，
     * 返回的URI内容则是通过ServiceInstance的服务实例详情拼接出的具体“host:post”形式的请求地址。
     */
    URI reconstructURI(ServiceInstance instance, URI original);
}
