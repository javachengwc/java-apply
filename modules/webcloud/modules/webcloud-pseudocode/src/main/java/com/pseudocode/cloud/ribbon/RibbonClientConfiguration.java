package com.pseudocode.cloud.ribbon;

import com.pseudocode.netflix.ribbon.core.client.DefaultLoadBalancerRetryHandler;
import com.pseudocode.netflix.ribbon.core.client.RetryHandler;
import com.pseudocode.netflix.ribbon.core.client.config.CommonClientConfigKey;
import com.pseudocode.netflix.ribbon.core.client.config.DefaultClientConfigImpl;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.cloud.pseudocode.ribbon.loadbalancer.*;
import com.pseudocode.netflix.ribbon.loadbalancer.DummyPing;
import com.pseudocode.netflix.ribbon.loadbalancer.ILoadBalancer;
import com.pseudocode.netflix.ribbon.loadbalancer.IPing;
import com.pseudocode.netflix.ribbon.loadbalancer.rule.ZoneAvoidanceRule;
import com.cloud.pseudocode.ribbon.loadbalancer.server.*;
import com.pseudocode.netflix.ribbon.loadbalancer.server.*;
import com.pseudocode.netflix.ribbon.loadbalancer.zone.ZoneAwareLoadBalancer;
import com.pseudocode.netflix.ribbon.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.net.URI;

//ribbon客户端自动装配配置
//在和Eureka配合使用时，会首先采用EurekaRibbonClientConfiguration配置类,它里面ribbonPing，ribbonServerList重新实例化了
@SuppressWarnings("deprecation")
@Configuration
@EnableConfigurationProperties
@Import({HttpClientConfiguration.class, OkHttpRibbonConfiguration.class, RestClientRibbonConfiguration.class, HttpClientRibbonConfiguration.class})
public class RibbonClientConfiguration {

    public static final int DEFAULT_CONNECT_TIMEOUT = 1000;
    public static final int DEFAULT_READ_TIMEOUT = 1000;

    @RibbonClientName
    private String name = "client";

    @Autowired
    private PropertiesFactory propertiesFactory;

    //默认的负载均衡器配置
    @Bean
    @ConditionalOnMissingBean
    public IClientConfig ribbonClientConfig() {
        DefaultClientConfigImpl config = new DefaultClientConfigImpl();
        config.loadProperties(this.name);
        config.set(CommonClientConfigKey.ConnectTimeout, DEFAULT_CONNECT_TIMEOUT);
        config.set(CommonClientConfigKey.ReadTimeout, DEFAULT_READ_TIMEOUT);
        return config;
    }

    //默认的负载均衡规则
    @Bean
    @ConditionalOnMissingBean
    public IRule ribbonRule(IClientConfig config) {
        if (this.propertiesFactory.isSet(IRule.class, name)) {
            return this.propertiesFactory.get(IRule.class, config, name);
        }
        ZoneAvoidanceRule rule = new ZoneAvoidanceRule();
        rule.initWithNiwsConfig(config);
        return rule;
    }

    //ping组件
    @Bean
    @ConditionalOnMissingBean
    public IPing ribbonPing(IClientConfig config) {
        if (this.propertiesFactory.isSet(IPing.class, name)) {
            return this.propertiesFactory.get(IPing.class, config, name);
        }
        return new DummyPing();
    }

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("unchecked")
    public ServerList<Server> ribbonServerList(IClientConfig config) {
        if (this.propertiesFactory.isSet(ServerList.class, name)) {
            return this.propertiesFactory.get(ServerList.class, config, name);
        }
        ConfigurationBasedServerList serverList = new ConfigurationBasedServerList();
        serverList.initWithNiwsConfig(config);
        return serverList;
    }

    //默认的robbon获取服务列表的定时更新器
    @Bean
    @ConditionalOnMissingBean
    public ServerListUpdater ribbonServerListUpdater(IClientConfig config) {
        return new PollingServerListUpdater(config);
    }

    //默认的负载均衡器为ZoneAwareLoadBalancer
    @Bean
    @ConditionalOnMissingBean
    public ILoadBalancer ribbonLoadBalancer(IClientConfig config,
                                            ServerList<Server> serverList, ServerListFilter<Server> serverListFilter,
                                            IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
        if (this.propertiesFactory.isSet(ILoadBalancer.class, name)) {
            return this.propertiesFactory.get(ILoadBalancer.class, config, name);
        }
        //config：DefaultClientConfigImp
        //rule：ZoneAvoidanceRule
        //ping：DummyPing
        //serverList：ConfigurationBasedServerList
        //serverListFilter：ZonePreferenceServerListFilter
        //serverListUpdater：PollingServerListUpdater
        return new ZoneAwareLoadBalancer<>(config, rule, ping, serverList,
                serverListFilter, serverListUpdater);
    }

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("unchecked")
    public ServerListFilter<Server> ribbonServerListFilter(IClientConfig config) {
        if (this.propertiesFactory.isSet(ServerListFilter.class, name)) {
            return this.propertiesFactory.get(ServerListFilter.class, config, name);
        }
        ZonePreferenceServerListFilter filter = new ZonePreferenceServerListFilter();
        filter.initWithNiwsConfig(config);
        return filter;
    }

    @Bean
    @ConditionalOnMissingBean
    public RibbonLoadBalancerContext ribbonLoadBalancerContext(ILoadBalancer loadBalancer,
                                                               IClientConfig config, RetryHandler retryHandler) {
        return new RibbonLoadBalancerContext(loadBalancer, config, retryHandler);
    }

    //重试处理器
    @Bean
    @ConditionalOnMissingBean
    public RetryHandler retryHandler(IClientConfig config) {
        return new DefaultLoadBalancerRetryHandler(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerIntrospector serverIntrospector() {
        return new DefaultServerIntrospector();
    }

    @PostConstruct
    public void preprocess() {
        setRibbonProperty(name, DeploymentContextBasedVipAddresses.key(), name);
    }

    static class OverrideRestClient extends RestClient {

        private IClientConfig config;
        private ServerIntrospector serverIntrospector;

        protected OverrideRestClient(IClientConfig config,
                                     ServerIntrospector serverIntrospector) {
            super();
            this.config = config;
            this.serverIntrospector = serverIntrospector;
            initWithNiwsConfig(this.config);
        }

        @Override
        public URI reconstructURIWithServer(Server server, URI original) {
            URI uri = updateToSecureConnectionIfNeeded(original, this.config,
                    this.serverIntrospector, server);
            return super.reconstructURIWithServer(server, uri);
        }

        @Override
        protected Client apacheHttpClientSpecificInitialization() {
            ApacheHttpClient4 apache = (ApacheHttpClient4) super.apacheHttpClientSpecificInitialization();
            apache.getClientHandler().getHttpClient().getParams().setParameter(
                    ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
            return apache;
        }

    }

}
