package com.pseudocode.netflix.ribbon.loadbalancer.zone;

import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.cloud.pseudocode.ribbon.loadbalancer.*;
import com.pseudocode.netflix.ribbon.loadbalancer.*;
import com.pseudocode.netflix.ribbon.loadbalancer.client.ClientFactory;
import com.pseudocode.netflix.ribbon.loadbalancer.rule.AvailabilityFilteringRule;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import com.pseudocode.netflix.ribbon.loadbalancer.server.ServerList;
import com.pseudocode.netflix.ribbon.loadbalancer.server.ServerListFilter;
import com.pseudocode.netflix.ribbon.loadbalancer.server.ServerListUpdater;
import com.google.common.annotations.VisibleForTesting;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicDoubleProperty;
import com.netflix.config.DynamicPropertyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//ZoneAwareLoadBalancer区域敏感的负载均衡器,是默认的负载均衡器
public class ZoneAwareLoadBalancer<T extends Server> extends DynamicServerListLoadBalancer<T> {

    private ConcurrentHashMap<String, BaseLoadBalancer> balancers = new ConcurrentHashMap<String, BaseLoadBalancer>();

    private static final Logger logger = LoggerFactory.getLogger(ZoneAwareLoadBalancer.class);

    private volatile DynamicDoubleProperty triggeringLoad;

    private volatile DynamicDoubleProperty triggeringBlackoutPercentage;

    private static final DynamicBooleanProperty ENABLED = DynamicPropertyFactory.getInstance().getBooleanProperty("ZoneAwareNIWSDiscoveryLoadBalancer.enabled", true);

    void setUpServerList(List<Server> upServerList) {
        this.upServerList = upServerList;
    }

    public ZoneAwareLoadBalancer() {
        super();
    }

    @Deprecated
    public ZoneAwareLoadBalancer(IClientConfig clientConfig, IRule rule,
                                 IPing ping, ServerList<T> serverList, ServerListFilter<T> filter) {
        super(clientConfig, rule, ping, serverList, filter);
    }

    //一般被用到的构造方法
    public ZoneAwareLoadBalancer(IClientConfig clientConfig, IRule rule,
                                 IPing ping, ServerList<T> serverList, ServerListFilter<T> filter,
                                 ServerListUpdater serverListUpdater) {
        super(clientConfig, rule, ping, serverList, filter, serverListUpdater);
    }

    public ZoneAwareLoadBalancer(IClientConfig niwsClientConfig) {
        super(niwsClientConfig);
    }

    @Override
    protected void setServerListForZones(Map<String, List<Server>> zoneServersMap) {
        super.setServerListForZones(zoneServersMap);
        if (balancers == null) {
            balancers = new ConcurrentHashMap<String, BaseLoadBalancer>();
        }
        for (Map.Entry<String, List<Server>> entry: zoneServersMap.entrySet()) {
            String zone = entry.getKey().toLowerCase();
            getLoadBalancer(zone).setServersList(entry.getValue());
        }
        // check if there is any zone that no longer has a server
        // and set the list to empty so that the zone related metrics does not
        // contain stale data
        for (Map.Entry<String, BaseLoadBalancer> existingLBEntry: balancers.entrySet()) {
            if (!zoneServersMap.keySet().contains(existingLBEntry.getKey())) {
                existingLBEntry.getValue().setServersList(Collections.emptyList());
            }
        }
    }

    //选择服务实例
    @Override
    public Server chooseServer(Object key) {
        if (!ENABLED.get() || getLoadBalancerStats().getAvailableZones().size() <= 1) {
            logger.debug("Zone aware logic disabled or there is only one zone");
            return super.chooseServer(key);
        }
        Server server = null;
        try {
            LoadBalancerStats lbStats = getLoadBalancerStats();
            Map<String, ZoneSnapshot> zoneSnapshot = ZoneAvoidanceRule.createSnapshot(lbStats);
            logger.debug("Zone snapshots: {}", zoneSnapshot);
            if (triggeringLoad == null) {
                triggeringLoad = DynamicPropertyFactory.getInstance().getDoubleProperty(
                        "ZoneAwareNIWSDiscoveryLoadBalancer." + this.getName() + ".triggeringLoadPerServerThreshold", 0.2d);
            }

            if (triggeringBlackoutPercentage == null) {
                triggeringBlackoutPercentage = DynamicPropertyFactory.getInstance().getDoubleProperty(
                        "ZoneAwareNIWSDiscoveryLoadBalancer." + this.getName() + ".avoidZoneWithBlackoutPercetage", 0.99999d);
            }
            Set<String> availableZones = ZoneAvoidanceRule.getAvailableZones(zoneSnapshot, triggeringLoad.get(), triggeringBlackoutPercentage.get());
            logger.debug("Available zones: {}", availableZones);
            if (availableZones != null &&  availableZones.size() < zoneSnapshot.keySet().size()) {
                String zone = ZoneAvoidanceRule.randomChooseZone(zoneSnapshot, availableZones);
                logger.debug("Zone chosen: {}", zone);
                if (zone != null) {
                    BaseLoadBalancer zoneLoadBalancer = getLoadBalancer(zone);
                    server = zoneLoadBalancer.chooseServer(key);
                }
            }
        } catch (Exception e) {
            logger.error("Error choosing server using zone aware logic for load balancer={}", name, e);
        }
        if (server != null) {
            return server;
        } else {
            logger.debug("Zone avoidance logic is not invoked.");
            return super.chooseServer(key);
        }
    }

    @VisibleForTesting
    BaseLoadBalancer getLoadBalancer(String zone) {
        zone = zone.toLowerCase();
        BaseLoadBalancer loadBalancer = balancers.get(zone);
        if (loadBalancer == null) {
            // We need to create rule object for load balancer for each zone
            IRule rule = cloneRule(this.getRule());
            loadBalancer = new BaseLoadBalancer(this.getName() + "_" + zone, rule, this.getLoadBalancerStats());
            BaseLoadBalancer prev = balancers.putIfAbsent(zone, loadBalancer);
            if (prev != null) {
                loadBalancer = prev;
            }
        }
        return loadBalancer;
    }

    private IRule cloneRule(IRule toClone) {
        IRule rule;
        if (toClone == null) {
            rule = new AvailabilityFilteringRule();
        } else {
            String ruleClass = toClone.getClass().getName();
            try {
                rule = (IRule) ClientFactory.instantiateInstanceWithClientConfig(ruleClass, this.getClientConfig());
            } catch (Exception e) {
                throw new RuntimeException("Unexpected exception creating rule for ZoneAwareLoadBalancer", e);
            }
        }
        return rule;
    }

    @Override
    public void setRule(IRule rule) {
        super.setRule(rule);
        if (balancers != null) {
            for (String zone: balancers.keySet()) {
                balancers.get(zone).setRule(cloneRule(rule));
            }
        }
    }
}
