package com.pseudocode.netflix.ribbon.loadbalancer.zone;

import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.*;
import com.pseudocode.netflix.ribbon.loadbalancer.client.ClientFactory;
import com.pseudocode.netflix.ribbon.loadbalancer.rule.AvailabilityFilteringRule;
import com.pseudocode.netflix.ribbon.loadbalancer.rule.ZoneAvoidanceRule;
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

    //zone-loadbalancer
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

    //重写了此方法，按区域Zone分组实例列表
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
    //一般情况下就是同区域下的服务实例
    //特殊情况下可能是多区域中选择一个服务实例
    //这里的特殊情况就是指的ZoneAffinityServerListFilter.shouldEnableZoneAffinity()方法返回false的情况
    @Override
    public Server chooseServer(Object key) {
        if (!ENABLED.get() || getLoadBalancerStats().getAvailableZones().size() <= 1) {
            logger.debug("Zone aware logic disabled or there is only one zone");
            return super.chooseServer(key);
        }
        Server server = null;
        try {
            LoadBalancerStats lbStats = getLoadBalancerStats();
            //为当前负载均衡器中所有的Zone区域分别创建快照
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
            //获取可用的Zone区域集合，在该函数中会通过Zone区域快照中的统计数据来实现可用区的挑选
            //首先剔除符合这些规则的Zone区域：所属实例数为零的Zone区域；Zone区域内实例平均负载小于零，或者实例故障率（断路器断开次数/实例数）大于等于阈值（默认为0.99999）
            //然后根据Zone区域的实例平均负载来计算出最差的Zone区域，这里的最差指的是实例平均负载最高的Zone区域。
            //如果在上面的过程中没有符合剔除要求的区域，同时实例最大平均负载小于阈值（默认为20%），就直接返回所有Zone区域为可用区域。
            //否则，从最坏Zone区域集合中随机的选择一个，将它从可用Zone区域集合中剔除
            Set<String> availableZones = ZoneAvoidanceRule.getAvailableZones(zoneSnapshot, triggeringLoad.get(), triggeringBlackoutPercentage.get());
            logger.debug("Available zones: {}", availableZones);
            //当获得的可用Zone区域集合不为空，并且个数小于Zone区域总数，就随机的选择一个Zone区域
            if (availableZones != null &&  availableZones.size() < zoneSnapshot.keySet().size()) {
                //随机的选择一个Zone区域
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
