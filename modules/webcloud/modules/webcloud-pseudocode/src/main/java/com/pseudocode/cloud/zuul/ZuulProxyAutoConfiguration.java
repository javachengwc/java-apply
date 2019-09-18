package com.pseudocode.cloud.zuul;


import java.util.Collections;
import java.util.List;

import com.pseudocode.cloud.commons.client.discovery.DiscoveryClient;
import com.pseudocode.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import com.pseudocode.cloud.commons.httpclient.ApacheHttpClientFactory;
import com.pseudocode.cloud.commons.httpclient.HttpClientConfiguration;
import com.pseudocode.cloud.zuul.filters.ZuulProperties;
import com.pseudocode.cloud.zuul.filters.route.RibbonCommandFactory;
import com.pseudocode.cloud.zuul.filters.route.RibbonRoutingFilter;
import com.pseudocode.cloud.zuul.filters.route.SimpleHostRoutingFilter;
import org.apache.http.impl.client.CloseableHttpClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//此装配类会初始化Zuul默认自带的Filter
@Configuration
@Import({ RibbonCommandFactoryConfiguration.RestClientRibbonConfiguration.class,
        RibbonCommandFactoryConfiguration.OkHttpRibbonConfiguration.class,
        RibbonCommandFactoryConfiguration.HttpClientRibbonConfiguration.class,
        HttpClientConfiguration.class })
@ConditionalOnBean(ZuulProxyMarkerConfiguration.Marker.class)
public class ZuulProxyAutoConfiguration extends ZuulServerAutoConfiguration {

    @SuppressWarnings("rawtypes")
    @Autowired(required = false)
    private List<RibbonRequestCustomizer> requestCustomizers = Collections.emptyList();

    @Autowired(required = false)
    private Registration registration;

    @Autowired
    private DiscoveryClient discovery;

    @Autowired
    private ServiceRouteMapper serviceRouteMapper;

    @Override
    public HasFeatures zuulFeature() {
        return HasFeatures.namedFeature("Zuul (Discovery)",
                ZuulProxyAutoConfiguration.class);
    }

    @Bean
    @ConditionalOnMissingBean(DiscoveryClientRouteLocator.class)
    public DiscoveryClientRouteLocator discoveryRouteLocator() {
        return new DiscoveryClientRouteLocator(this.server.getServlet().getServletPrefix(), this.discovery, this.zuulProperties,
                this.serviceRouteMapper, this.registration);
    }

    // pre filters
    @Bean
    @ConditionalOnMissingBean(PreDecorationFilter.class)
    public PreDecorationFilter preDecorationFilter(RouteLocator routeLocator, ProxyRequestHelper proxyRequestHelper) {
        return new PreDecorationFilter(routeLocator, this.server.getServlet().getServletPrefix(), this.zuulProperties,
                proxyRequestHelper);
    }

    // route filters
    //RibbonRoutingFilter,主要是完成请求的路由转发
    @Bean
    @ConditionalOnMissingBean(RibbonRoutingFilter.class)
    public RibbonRoutingFilter ribbonRoutingFilter(ProxyRequestHelper helper,
                                                   RibbonCommandFactory<?> ribbonCommandFactory) {
        RibbonRoutingFilter filter = new RibbonRoutingFilter(helper, ribbonCommandFactory, this.requestCustomizers);
        return filter;
    }

    @Bean
    @ConditionalOnMissingBean({SimpleHostRoutingFilter.class, CloseableHttpClient.class})
    public SimpleHostRoutingFilter simpleHostRoutingFilter(ProxyRequestHelper helper,
                                                           ZuulProperties zuulProperties,
                                                           ApacheHttpClientConnectionManagerFactory connectionManagerFactory,
                                                           ApacheHttpClientFactory httpClientFactory) {
        return new SimpleHostRoutingFilter(helper, zuulProperties,
                connectionManagerFactory, httpClientFactory);
    }

    @Bean
    @ConditionalOnMissingBean({SimpleHostRoutingFilter.class})
    public SimpleHostRoutingFilter simpleHostRoutingFilter2(ProxyRequestHelper helper,
                                                            ZuulProperties zuulProperties,
                                                            CloseableHttpClient httpClient) {
        return new SimpleHostRoutingFilter(helper, zuulProperties,
                httpClient);
    }

    @Bean
    @ConditionalOnMissingBean(ServiceRouteMapper.class)
    public ServiceRouteMapper serviceRouteMapper() {
        return new SimpleServiceRouteMapper();
    }

    @Configuration
    @ConditionalOnMissingClass("org.springframework.boot.actuate.endpoint.Endpoint")
    protected static class NoActuatorConfiguration {

        @Bean
        public ProxyRequestHelper proxyRequestHelper(ZuulProperties zuulProperties) {
            ProxyRequestHelper helper = new ProxyRequestHelper();
            helper.setIgnoredHeaders(zuulProperties.getIgnoredHeaders());
            helper.setTraceRequestBody(zuulProperties.isTraceRequestBody());
            return helper;
        }

    }

    @Configuration
    @ConditionalOnClass(Health.class)
    protected static class EndpointConfiguration {

        @Autowired(required = false)
        private HttpTraceRepository traces;

        @Bean
        @ConditionalOnEnabledEndpoint
        public RoutesEndpoint routesEndpoint(RouteLocator routeLocator) {
            return new RoutesEndpoint(routeLocator);
        }

        @ConditionalOnEnabledEndpoint
        @Bean
        public FiltersEndpoint filtersEndpoint() {
            FilterRegistry filterRegistry = FilterRegistry.instance();
            return new FiltersEndpoint(filterRegistry);
        }

        @Bean
        public ProxyRequestHelper proxyRequestHelper(ZuulProperties zuulProperties) {
            TraceProxyRequestHelper helper = new TraceProxyRequestHelper();
            if (this.traces != null) {
                helper.setTraces(this.traces);
            }
            helper.setIgnoredHeaders(zuulProperties.getIgnoredHeaders());
            helper.setTraceRequestBody(zuulProperties.isTraceRequestBody());
            return helper;
        }
    }
}
