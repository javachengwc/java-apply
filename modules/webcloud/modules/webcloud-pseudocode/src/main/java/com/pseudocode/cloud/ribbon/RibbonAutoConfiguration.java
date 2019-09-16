package com.pseudocode.cloud.ribbon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import com.pseudocode.cloud.commons.client.loadbalancer.LoadBalancedRetryFactory;
import com.pseudocode.cloud.commons.client.loadbalancer.LoadBalancerAutoConfiguration;
import com.pseudocode.cloud.commons.client.loadbalancer.LoadBalancerClient;
import com.pseudocode.netflix.ribbon.core.client.IClient;
import com.pseudocode.netflix.ribbon.httpclient.http.HttpRequest;
import com.pseudocode.netflix.ribbon.ribbon.Ribbon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
@Configuration
@ConditionalOnClass({ IClient.class, RestTemplate.class, AsyncRestTemplate.class, Ribbon.class})
@RibbonClients
@AutoConfigureAfter(name = "org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration")
@AutoConfigureBefore({LoadBalancerAutoConfiguration.class, AsyncLoadBalancerAutoConfiguration.class})
@EnableConfigurationProperties({RibbonEagerLoadProperties.class, ServerIntrospectorProperties.class})
public class RibbonAutoConfiguration {

    @Autowired(required = false)
    private List<RibbonClientSpecification> configurations = new ArrayList<>();

    @Autowired
    private RibbonEagerLoadProperties ribbonEagerLoadProperties;

    @Bean
    public HasFeatures ribbonFeature() {
        return HasFeatures.namedFeature("Ribbon", Ribbon.class);
    }

    @Bean
    public SpringClientFactory springClientFactory() {
        SpringClientFactory factory = new SpringClientFactory();
        factory.setConfigurations(this.configurations);
        return factory;
    }

    //负载均衡客户端
    @Bean
    @ConditionalOnMissingBean(LoadBalancerClient.class)
    public LoadBalancerClient loadBalancerClient() {
        return new RibbonLoadBalancerClient(springClientFactory());
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.retry.support.RetryTemplate")
    @ConditionalOnMissingBean
    public LoadBalancedRetryFactory loadBalancedRetryPolicyFactory(final SpringClientFactory clientFactory) {
        return new RibbonLoadBalancedRetryFactory(clientFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public PropertiesFactory propertiesFactory() {
        return new PropertiesFactory();
    }

    @Bean
    @ConditionalOnProperty(value = "ribbon.eager-load.enabled")
    public RibbonApplicationContextInitializer ribbonApplicationContextInitializer() {
        return new RibbonApplicationContextInitializer(springClientFactory(), ribbonEagerLoadProperties.getClients());
    }

    @Configuration
    @ConditionalOnClass(HttpRequest.class)
    @ConditionalOnRibbonRestClient
    protected static class RibbonClientHttpRequestFactoryConfiguration {

        @Autowired
        private SpringClientFactory springClientFactory;

        @Bean
        public RestTemplateCustomizer restTemplateCustomizer(
                final RibbonClientHttpRequestFactory ribbonClientHttpRequestFactory) {
            return restTemplate -> restTemplate.setRequestFactory(ribbonClientHttpRequestFactory);
        }

        @Bean
        public RibbonClientHttpRequestFactory ribbonClientHttpRequestFactory() {
            return new RibbonClientHttpRequestFactory(this.springClientFactory);
        }
    }

    //TODO: support for autoconfiguring restemplate to use apache http client or okhttp
    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Conditional(OnRibbonRestClientCondition.class)
    @interface ConditionalOnRibbonRestClient { }

    private static class OnRibbonRestClientCondition extends AnyNestedCondition {
        public OnRibbonRestClientCondition() {
            super(ConfigurationPhase.REGISTER_BEAN);
        }

        @Deprecated //remove in Edgware"
        @ConditionalOnProperty("ribbon.http.client.enabled")
        static class ZuulProperty {}

        @ConditionalOnProperty("ribbon.restclient.enabled")
        static class RibbonProperty {}
    }
}

