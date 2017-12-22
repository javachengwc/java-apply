package com.component.rest.jersey.balance;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.context.annotation.Profile;

@Profile("microservice")
public class LoadBalanceRule extends AvailabilityFilteringRule {

    private AbstractServerPredicate childPredicate;

    private volatile AbstractLoadBalancerRule childRoundRobinRule;

    public LoadBalanceRule() {
        super();
        childRoundRobinRule = new RoundRobinRule();
        childPredicate = CompositePredicate.withPredicates(super.getPredicate(), new VersionPredicate()).build();
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        childRoundRobinRule = new RoundRobinRule();
    }

    @Override
    public void setLoadBalancer(ILoadBalancer loadBalancer) {
        super.setLoadBalancer(loadBalancer);
        childRoundRobinRule.setLoadBalancer(loadBalancer);
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        if (null == childPredicate) {
            return super.getPredicate();
        }
        return childPredicate;
    }

    @Override
    public Server choose(Object key) {
        int count = 0;
        Server server = childRoundRobinRule.choose(key);
        while (count<= 9) {
            if (getPredicate().apply(new PredicateKey(server))) {
                return server;
            }
            server = childRoundRobinRule.choose(key);
            count++;
        }
        return super.choose(key);
    }

    public AbstractLoadBalancerRule getChildRoundRobinRule() {
        return childRoundRobinRule;
    }

    public void setChildRoundRobinRule(AbstractLoadBalancerRule childRoundRobinRule) {
        childRoundRobinRule.setLoadBalancer(this.getLoadBalancer());
        this.childRoundRobinRule = childRoundRobinRule;
    }
}
