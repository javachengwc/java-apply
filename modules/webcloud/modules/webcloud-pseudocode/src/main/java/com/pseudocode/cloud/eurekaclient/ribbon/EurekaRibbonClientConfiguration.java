package com.pseudocode.cloud.eurekaclient.ribbon;

import com.pseudocode.cloud.ribbon.RibbonUtils;
import com.pseudocode.cloud.ribbon.ServerIntrospector;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.server.ServerList;
import com.pseudocode.netflix.ribbon.loadbalancer.IPing;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;
import com.netflix.discovery.EurekaClientConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

//ping：NIWSDiscoveryPing。
//serverList：DomainExtractingServerList，内部是DiscoveryEnabledNIWSServerList，实际上是通过服务发现获取服务信息列表。
@Configuration
public class EurekaRibbonClientConfiguration {

    private static final Log log = LogFactory.getLog(EurekaRibbonClientConfiguration.class);

    @Value("${ribbon.eureka.approximateZoneFromHostname:false}")
    private boolean approximateZoneFromHostname = false;

    @RibbonClientName
    private String serviceId = "client";

    @Autowired(required = false)
    private EurekaClientConfig clientConfig;

    @Autowired(required = false)
    private EurekaInstanceConfig eurekaConfig;

    @Autowired
    private PropertiesFactory propertiesFactory;

    public EurekaRibbonClientConfiguration() {
    }

    public EurekaRibbonClientConfiguration(EurekaClientConfig clientConfig,
                                           String serviceId, EurekaInstanceConfig eurekaConfig,
                                           boolean approximateZoneFromHostname) {
        this.clientConfig = clientConfig;
        this.serviceId = serviceId;
        this.eurekaConfig = eurekaConfig;
        this.approximateZoneFromHostname = approximateZoneFromHostname;
    }

    @Bean
    @ConditionalOnMissingBean
    public IPing ribbonPing(IClientConfig config) {
        if (this.propertiesFactory.isSet(IPing.class, serviceId)) {
            return this.propertiesFactory.get(IPing.class, config, serviceId);
        }
        NIWSDiscoveryPing ping = new NIWSDiscoveryPing();
        ping.initWithNiwsConfig(config);
        return ping;
    }

    //serverList：DomainExtractingServerList，内部是DiscoveryEnabledNIWSServerList，实际上是通过服务发现获取服务信息列表。
    @Bean
    @ConditionalOnMissingBean
    public ServerList<?> ribbonServerList(IClientConfig config, Provider<EurekaClient> eurekaClientProvider) {
        if (this.propertiesFactory.isSet(ServerList.class, serviceId)) {
            return this.propertiesFactory.get(ServerList.class, config, serviceId);
        }
        DiscoveryEnabledNIWSServerList discoveryServerList = new DiscoveryEnabledNIWSServerList(
                config, eurekaClientProvider);
        DomainExtractingServerList serverList = new DomainExtractingServerList(
                discoveryServerList, config, this.approximateZoneFromHostname);
        return serverList;
    }

    @Bean
    public ServerIntrospector serverIntrospector() {
        return new EurekaServerIntrospector();
    }

    @PostConstruct
    public void preprocess() {
        String zone = ConfigurationManager.getDeploymentContext()
                .getValue(DeploymentContext.ContextKey.zone);
        if (this.clientConfig != null && StringUtils.isEmpty(zone)) {
            if (this.approximateZoneFromHostname && this.eurekaConfig != null) {
                String approxZone = ZoneUtils
                        .extractApproximateZone(this.eurekaConfig.getHostName(false));
                log.debug("Setting Zone To " + approxZone);
                ConfigurationManager.getDeploymentContext().setValue(DeploymentContext.ContextKey.zone,
                        approxZone);
            }
            else {
                String availabilityZone = this.eurekaConfig == null ? null
                        : this.eurekaConfig.getMetadataMap().get("zone");
                if (availabilityZone == null) {
                    String[] zones = this.clientConfig
                            .getAvailabilityZones(this.clientConfig.getRegion());
                    // Pick the first one from the regions we want to connect to
                    availabilityZone = zones != null && zones.length > 0 ? zones[0]
                            : null;
                }
                if (availabilityZone != null) {
                    // You can set this with archaius.deployment.* (maybe requires
                    // custom deployment context)?
                    ConfigurationManager.getDeploymentContext().setValue(DeploymentContext.ContextKey.zone,
                            availabilityZone);
                }
            }
        }
        RibbonUtils.initializeRibbonDefaults(serviceId);
    }

}
