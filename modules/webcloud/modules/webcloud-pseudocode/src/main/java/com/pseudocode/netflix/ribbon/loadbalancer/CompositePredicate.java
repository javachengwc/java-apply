package com.pseudocode.netflix.ribbon.loadbalancer;


import java.util.Iterator;
import java.util.List;


import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.pseudocode.netflix.ribbon.loadbalancer.rule.AbstractServerPredicate;
import com.pseudocode.netflix.ribbon.loadbalancer.rule.PredicateKey;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

public class CompositePredicate extends AbstractServerPredicate {

    private AbstractServerPredicate delegate;

    private List<AbstractServerPredicate> fallbacks = Lists.newArrayList();

    private int minimalFilteredServers = 1;

    private float minimalFilteredPercentage = 0;

    @Override
    public boolean apply(PredicateKey input) {
        return delegate.apply(input);
    }


    public static class Builder {

        private CompositePredicate toBuild;

        Builder(AbstractServerPredicate primaryPredicate) {
            toBuild = new CompositePredicate();
            toBuild.delegate = primaryPredicate;
        }

        Builder(AbstractServerPredicate ...primaryPredicates) {
            toBuild = new CompositePredicate();
            Predicate<PredicateKey> chain = Predicates.<PredicateKey>and(primaryPredicates);
            toBuild.delegate =  AbstractServerPredicate.ofKeyPredicate(chain);
        }

        public Builder addFallbackPredicate(AbstractServerPredicate fallback) {
            toBuild.fallbacks.add(fallback);
            return this;
        }

        public Builder setFallbackThresholdAsMinimalFilteredNumberOfServers(int number) {
            toBuild.minimalFilteredServers = number;
            return this;
        }

        public Builder setFallbackThresholdAsMinimalFilteredPercentage(float percent) {
            toBuild.minimalFilteredPercentage = percent;
            return this;
        }

        public CompositePredicate build() {
            return toBuild;
        }
    }

    public static Builder withPredicates(AbstractServerPredicate ...primaryPredicates) {
        return new Builder(primaryPredicates);
    }

    public static Builder withPredicate(AbstractServerPredicate primaryPredicate) {
        return new Builder(primaryPredicate);
    }

    @Override
    public List<Server> getEligibleServers(List<Server> servers, Object loadBalancerKey) {
        List<Server> result = super.getEligibleServers(servers, loadBalancerKey);
        Iterator<AbstractServerPredicate> i = fallbacks.iterator();
        while (!(result.size() >= minimalFilteredServers && result.size() > (int) (servers.size() * minimalFilteredPercentage))
                && i.hasNext()) {
            AbstractServerPredicate predicate = i.next();
            result = predicate.getEligibleServers(servers, loadBalancerKey);
        }
        return result;
    }
}
