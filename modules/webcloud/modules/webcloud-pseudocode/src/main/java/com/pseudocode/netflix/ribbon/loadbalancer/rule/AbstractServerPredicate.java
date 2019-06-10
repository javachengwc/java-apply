package com.pseudocode.netflix.ribbon.loadbalancer.rule;

import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.cloud.pseudocode.ribbon.loadbalancer.*;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.pseudocode.netflix.ribbon.loadbalancer.AbstractLoadBalancer;
import com.pseudocode.netflix.ribbon.loadbalancer.ILoadBalancer;
import com.pseudocode.netflix.ribbon.loadbalancer.IRule;
import com.pseudocode.netflix.ribbon.loadbalancer.LoadBalancerStats;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractServerPredicate implements Predicate<PredicateKey> {

    protected IRule rule;
    private volatile LoadBalancerStats lbStats;

    private final Random random = new Random();

    private final AtomicInteger nextIndex = new AtomicInteger();

    private final Predicate<Server> serverOnlyPredicate =  new Predicate<Server>() {
        @Override
        public boolean apply(@Nullable Server input) {
            return AbstractServerPredicate.this.apply(new PredicateKey(input));
        }
    };

    public static AbstractServerPredicate alwaysTrue() {
        return new AbstractServerPredicate() {
            @Override
            public boolean apply(@Nullable PredicateKey input) {
                return true;
            }
        };
    }

    public AbstractServerPredicate() {

    }

    public AbstractServerPredicate(IRule rule) {
        this.rule = rule;
    }

    public AbstractServerPredicate(IRule rule, IClientConfig clientConfig) {
        this.rule = rule;
    }

    public AbstractServerPredicate(LoadBalancerStats lbStats, IClientConfig clientConfig) {
        this.lbStats = lbStats;
    }

    protected LoadBalancerStats getLBStats() {
        if (lbStats != null) {
            return lbStats;
        } else if (rule != null) {
            ILoadBalancer lb = rule.getLoadBalancer();
            if (lb instanceof AbstractLoadBalancer) {
                LoadBalancerStats stats =  ((AbstractLoadBalancer) lb).getLoadBalancerStats();
                setLoadBalancerStats(stats);
                return stats;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void setLoadBalancerStats(LoadBalancerStats stats) {
        this.lbStats = stats;
    }

    public Predicate<Server> getServerOnlyPredicate() {
        return serverOnlyPredicate;
    }

    public List<Server> getEligibleServers(List<Server> servers) {
        return getEligibleServers(servers, null);
    }

    public List<Server> getEligibleServers(List<Server> servers, Object loadBalancerKey) {
        if (loadBalancerKey == null) {
            return ImmutableList.copyOf(Iterables.filter(servers, this.getServerOnlyPredicate()));
        } else {
            List<Server> results = Lists.newArrayList();
            for (Server server: servers) {
                if (this.apply(new PredicateKey(loadBalancerKey, server))) {
                    results.add(server);
                }
            }
            return results;
        }
    }

    private int incrementAndGetModulo(int modulo) {
        for (;;) {
            int current = nextIndex.get();
            int next = (current + 1) % modulo;
            if (nextIndex.compareAndSet(current, next) && current < modulo)
                return current;
        }
    }

    public Optional<Server> chooseRandomlyAfterFiltering(List<Server> servers) {
        List<Server> eligible = getEligibleServers(servers);
        if (eligible.size() == 0) {
            return Optional.absent();
        }
        return Optional.of(eligible.get(random.nextInt(eligible.size())));
    }

    public Optional<Server> chooseRoundRobinAfterFiltering(List<Server> servers) {
        List<Server> eligible = getEligibleServers(servers);
        if (eligible.size() == 0) {
            return Optional.absent();
        }
        return Optional.of(eligible.get(incrementAndGetModulo(eligible.size())));
    }

    public Optional<Server> chooseRandomlyAfterFiltering(List<Server> servers, Object loadBalancerKey) {
        List<Server> eligible = getEligibleServers(servers, loadBalancerKey);
        if (eligible.size() == 0) {
            return Optional.absent();
        }
        return Optional.of(eligible.get(random.nextInt(eligible.size())));
    }

    public Optional<Server> chooseRoundRobinAfterFiltering(List<Server> servers, Object loadBalancerKey) {
        List<Server> eligible = getEligibleServers(servers, loadBalancerKey);
        if (eligible.size() == 0) {
            return Optional.absent();
        }
        return Optional.of(eligible.get(incrementAndGetModulo(eligible.size())));
    }

    public static AbstractServerPredicate ofKeyPredicate(final Predicate<PredicateKey> p) {
        return new AbstractServerPredicate() {
            public boolean apply(PredicateKey input) {
                return p.apply(input);
            }
        };
    }

    public static AbstractServerPredicate ofServerPredicate(final Predicate<Server> p) {
        return new AbstractServerPredicate() {
            public boolean apply(PredicateKey input) {
                return p.apply(input.getServer());
            }
        };
    }
}
