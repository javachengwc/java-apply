package com.cloud.pseudocode.ribbon.loadbalancer.rule;

import com.cloud.pseudocode.ribbon.core.client.config.IClientConfig;
import com.cloud.pseudocode.ribbon.loadbalancer.ILoadBalancer;
import com.cloud.pseudocode.ribbon.loadbalancer.Server;

public class ClientConfigEnabledRoundRobinRule extends AbstractLoadBalancerRule {

    RoundRobinRule roundRobinRule = new RoundRobinRule();

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        roundRobinRule = new RoundRobinRule();
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        roundRobinRule.setLoadBalancer(lb);
    }

    @Override
    public Server choose(Object key) {
        if (roundRobinRule != null) {
            return roundRobinRule.choose(key);
        } else {
            throw new IllegalArgumentException(
                    "This class has not been initialized with the RoundRobinRule class");
        }
    }

}
