package com.cloud.pseudocode.ribbon.loadbalancer.rule;

import com.cloud.pseudocode.ribbon.loadbalancer.ILoadBalancer;
import com.cloud.pseudocode.ribbon.loadbalancer.Server;
import com.google.common.base.Optional;

//基于Predicate策略的规则
public abstract class PredicateBasedRule extends ClientConfigEnabledRoundRobinRule {

    public abstract AbstractServerPredicate getPredicate();

    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
        if (server.isPresent()) {
            return server.get();
        } else {
            return null;
        }
    }
}
