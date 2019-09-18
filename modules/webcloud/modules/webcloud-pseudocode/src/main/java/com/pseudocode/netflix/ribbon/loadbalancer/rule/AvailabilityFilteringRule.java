package com.pseudocode.netflix.ribbon.loadbalancer.rule;


import java.util.List;

import com.google.common.collect.Collections2;
import com.netflix.servo.annotations.DataSourceType;
import com.netflix.servo.annotations.Monitor;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.CompositePredicate;
import com.pseudocode.netflix.ribbon.loadbalancer.ILoadBalancer;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;


public class AvailabilityFilteringRule extends PredicateBasedRule {

    private AbstractServerPredicate predicate;

    public AvailabilityFilteringRule() {
        super();
        predicate = CompositePredicate.withPredicate(new AvailabilityPredicate(this, null))
                .addFallbackPredicate(AbstractServerPredicate.alwaysTrue())
                .build();
    }


    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        predicate = CompositePredicate.withPredicate(new AvailabilityPredicate(this, clientConfig))
                .addFallbackPredicate(AbstractServerPredicate.alwaysTrue())
                .build();
    }

    @Monitor(name="AvailableServersCount", type = DataSourceType.GAUGE)
    public int getAvailableServersCount() {
        ILoadBalancer lb = getLoadBalancer();
        List<Server> servers = lb.getAllServers();
        if (servers == null) {
            return 0;
        }
        return Collections2.filter(servers, predicate.getServerOnlyPredicate()).size();
    }

    @Override
    public Server choose(Object key) {
        int count = 0;
        Server server = roundRobinRule.choose(key);
        while (count++ <= 10) {
            if (predicate.apply(new PredicateKey(server))) {
                return server;
            }
            server = roundRobinRule.choose(key);
        }
        return super.choose(key);
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        return predicate;
    }
}

