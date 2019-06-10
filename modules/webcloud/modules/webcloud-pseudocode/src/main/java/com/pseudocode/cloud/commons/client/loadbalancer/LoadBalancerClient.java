package com.pseudocode.cloud.commons.client.loadbalancer;

import com.pseudocode.cloud.commons.client.ServiceInstance;

import java.io.IOException;
import java.net.URI;

public interface LoadBalancerClient extends ServiceInstanceChooser {

    <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException;

    <T> T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest<T> request) throws IOException;

    /**
     * 为系统构建一个合适的host:port形式的url。
     * 在分布式系统中，使用逻辑上的服务名称作为host来构建URI
     * （替代服务实例的host:port形式）进行请求，比如说myservice/path/to/service
     */
    URI reconstructURI(ServiceInstance instance, URI original);
}
