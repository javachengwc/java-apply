package com.cloud.pseudocode.common.client.loadbalancer;

import com.cloud.pseudocode.common.client.ServiceInstance;

public interface ServiceInstanceChooser {

    ServiceInstance choose(String var1);
}
