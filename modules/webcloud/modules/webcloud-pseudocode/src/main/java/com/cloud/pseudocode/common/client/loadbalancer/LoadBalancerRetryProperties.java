package com.cloud.pseudocode.common.client.loadbalancer;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
