package com.pseudocode.cloud.commons.client.loadbalancer;

import com.pseudocode.cloud.commons.client.ServiceInstance;

public interface ServiceInstanceChooser {

    //根据传入的服务名serviceId，从负载均衡器中挑选一个对应服务的实例
    ServiceInstance choose(String serviceId);
}
