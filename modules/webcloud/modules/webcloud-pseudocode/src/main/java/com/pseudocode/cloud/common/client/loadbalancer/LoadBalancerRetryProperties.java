package com.pseudocode.cloud.common.client.loadbalancer;

import org.springframework.boot.context.properties.ConfigurationProperties;

//是否开启重试，默认开启
//spring.cloud.loadbalancer.retry.enabled=false 来关闭重试
@ConfigurationProperties("spring.cloud.loadbalancer.retry")
public class LoadBalancerRetryProperties {

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
