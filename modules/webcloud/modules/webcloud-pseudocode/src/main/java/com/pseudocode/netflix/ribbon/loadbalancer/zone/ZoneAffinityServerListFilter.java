package com.pseudocode.netflix.ribbon.loadbalancer.zone;

import com.pseudocode.netflix.ribbon.core.client.IClientConfigAware;
import com.pseudocode.netflix.ribbon.core.client.config.CommonClientConfigKey;
import com.pseudocode.netflix.ribbon.core.client.config.DefaultClientConfigImpl;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.server.AbstractServerListFilter;
import com.pseudocode.netflix.ribbon.loadbalancer.LoadBalancerStats;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.netflix.config.*;
import com.netflix.servo.monitor.Counter;
import com.netflix.servo.monitor.Monitors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//ZoneAffinityServerListFilter会根据配置选择和服务消费者在一个zone的服务（术语区域感知，Zone Affinity），这样一般可以降低延迟。
public class ZoneAffinityServerListFilter<T extends Server> extends
        AbstractServerListFilter<T> implements IClientConfigAware {

    //区域感知的
    private volatile boolean zoneAffinity = DefaultClientConfigImpl.DEFAULT_ENABLE_ZONE_AFFINITY;

    //区域专有的
    private volatile boolean zoneExclusive = DefaultClientConfigImpl.DEFAULT_ENABLE_ZONE_EXCLUSIVITY;

    private DynamicDoubleProperty activeReqeustsPerServerThreshold;

    //故障实例百分比
    private DynamicDoubleProperty blackOutServerPercentageThreshold;
    private DynamicIntProperty availableServersThreshold;
    private Counter overrideCounter;
    private ZoneAffinityPredicate zoneAffinityPredicate = new ZoneAffinityPredicate();

    private static Logger logger = LoggerFactory.getLogger(ZoneAffinityServerListFilter.class);

    String zone;

    public ZoneAffinityServerListFilter() {
    }

    public ZoneAffinityServerListFilter(IClientConfig niwsClientConfig) {
        initWithNiwsConfig(niwsClientConfig);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig niwsClientConfig) {
        String sZoneAffinity = "" + niwsClientConfig.getProperty(CommonClientConfigKey.EnableZoneAffinity, false);
        if (sZoneAffinity != null){
            zoneAffinity = Boolean.parseBoolean(sZoneAffinity);
            logger.debug("ZoneAffinity is set to {}", zoneAffinity);
        }
        String sZoneExclusive = "" + niwsClientConfig.getProperty(CommonClientConfigKey.EnableZoneExclusivity, false);
        if (sZoneExclusive != null){
            zoneExclusive = Boolean.parseBoolean(sZoneExclusive);
        }
        if (ConfigurationManager.getDeploymentContext() != null) {
            zone = ConfigurationManager.getDeploymentContext().getValue(DeploymentContext.ContextKey.zone);
        }
        activeReqeustsPerServerThreshold = DynamicPropertyFactory.getInstance().getDoubleProperty(niwsClientConfig.getClientName() + "." + niwsClientConfig.getNameSpace() + ".zoneAffinity.maxLoadPerServer", 0.6d);
        logger.debug("activeReqeustsPerServerThreshold: {}", activeReqeustsPerServerThreshold.get());
        blackOutServerPercentageThreshold = DynamicPropertyFactory.getInstance().getDoubleProperty(niwsClientConfig.getClientName() + "." + niwsClientConfig.getNameSpace() + ".zoneAffinity.maxBlackOutServesrPercentage", 0.8d);
        logger.debug("blackOutServerPercentageThreshold: {}", blackOutServerPercentageThreshold.get());
        availableServersThreshold = DynamicPropertyFactory.getInstance().getIntProperty(niwsClientConfig.getClientName() + "." + niwsClientConfig.getNameSpace() + ".zoneAffinity.minAvailableServers", 2);
        logger.debug("availableServersThreshold: {}", availableServersThreshold.get());
        overrideCounter = Monitors.newCounter("ZoneAffinity_OverrideCounter");

        Monitors.registerObject("NIWSServerListFilter_" + niwsClientConfig.getClientName());
    }

    //判断是否开启这个区域感知
    //如果过滤后的后端服务器忙，则停止区域感知。即基于实时的服务器运行状况，决定是否只选择本区域的server。
    private boolean shouldEnableZoneAffinity(List<T> filtered) {
        if (!zoneAffinity && !zoneExclusive) {
            return false;
        }
        if (zoneExclusive) {
            return true;
        }
        LoadBalancerStats stats = getLoadBalancerStats();
        if (stats == null) {
            return zoneAffinity;
        } else {
            logger.debug("Determining if zone affinity should be enabled with given server list: {}", filtered);
            ZoneSnapshot snapshot = stats.getZoneSnapshot(filtered);
            double loadPerServer = snapshot.getLoadPerServer(); //实例平均负载
            int instanceCount = snapshot.getInstanceCount();//实例数量
            int circuitBreakerTrippedCount = snapshot.getCircuitTrippedCount();//断路器断开数
            //故障实例百分比（断路器断开数 / 实例数量） >= 0.8
            if (((double) circuitBreakerTrippedCount) / instanceCount >= blackOutServerPercentageThreshold.get()
                    //实例平均负载 >= 0.6
                    || loadPerServer >= activeReqeustsPerServerThreshold.get()
                    //可用实例数（实例数量 - 断路器断开数） < 2
                    || (instanceCount - circuitBreakerTrippedCount) < availableServersThreshold.get()) {
                logger.debug("zoneAffinity is overriden. blackOutServerPercentage: {}, activeReqeustsPerServer: {}, availableServers: {}",
                        new Object[] {(double) circuitBreakerTrippedCount / instanceCount,  loadPerServer, instanceCount - circuitBreakerTrippedCount});
                return false;
            } else {
                return true;
            }

        }
    }


    @Override
    public List<T> getFilteredListOfServers(List<T> servers) {
        if (zone != null && (zoneAffinity || zoneExclusive) && servers !=null && servers.size() > 0){
            //刷选出地域匹配的服务
            List<T> filteredServers = Lists.newArrayList(Iterables.filter(
                    servers, this.zoneAffinityPredicate.getServerOnlyPredicate()));
            if (shouldEnableZoneAffinity(filteredServers)) {
                return filteredServers;
            } else if (zoneAffinity) {
                overrideCounter.increment();
            }
        }
        return servers;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("ZoneAffinityServerListFilter:");
        sb.append(", zone: ").append(zone).append(", zoneAffinity:").append(zoneAffinity);
        sb.append(", zoneExclusivity:").append(zoneExclusive);
        return sb.toString();
    }
}
