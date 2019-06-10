package com.pseudocode.cloud.commons.client.loadbalancer;

import com.pseudocode.cloud.commons.client.ServiceInstance;

public interface ServiceInstanceChooser {

    ServiceInstance choose(String var1);
}
