package com.pseudocode.cloud.common.client.loadbalancer;

import com.pseudocode.cloud.common.client.ServiceInstance;

public interface ServiceInstanceChooser {

    ServiceInstance choose(String var1);
}
