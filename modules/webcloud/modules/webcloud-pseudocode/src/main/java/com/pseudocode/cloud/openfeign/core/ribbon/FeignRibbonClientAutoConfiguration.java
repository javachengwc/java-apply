package com.pseudocode.cloud.openfeign.core.ribbon;


import com.pseudocode.cloud.commons.client.loadbalancer.LoadBalancedRetryFactory;
import com.pseudocode.cloud.openfeign.core.FeignAutoConfiguration;
import com.pseudocode.cloud.openfeign.core.support.FeignHttpClientProperties;
import com.pseudocode.cloud.ribbon.SpringClientFactory;
import com.pseudocode.netflix.feign.core.Feign;
import com.pseudocode.netflix.feign.core.Request;
import com.pseudocode.netflix.ribbon.loadbalancer.ILoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;


@ConditionalOnClass({ ILoadBalancer.class, Feign.class })
@Configuration
@AutoConfigureBefore(FeignAutoConfiguration.class)
@EnableConfigurationProperties({ FeignHttpClientProperties.class })
//Order is important here, last should be the default, first should be optional
// see https://github.com/spring-cloud/spring-cloud-netflix/issues/2086#issuecomment-316281653
@Import({ HttpClientFeignLoadBalancedConfiguration.class,
        OkHttpFeignLoadBalancedConfiguration.class,
        DefaultFeignLoadBalancedConfiguration.class })
public class FeignRibbonClientAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingClass("org.springframework.retry.support.RetryTemplate")
    public CachingSpringLoadBalancerFactory cachingLBClientFactory(SpringClientFactory factory) {
        return new CachingSpringLoadBalancerFactory(factory);
    }

    @Bean
    @Primary
    @ConditionalOnClass(name = "org.springframework.retry.support.RetryTemplate")
    public CachingSpringLoadBalancerFactory retryabeCachingLBClientFactory(
            SpringClientFactory factory,
            LoadBalancedRetryFactory retryFactory) {
        return new CachingSpringLoadBalancerFactory(factory, retryFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public Request.Options feignRequestOptions() {
        return LoadBalancerFeignClient.DEFAULT_OPTIONS;
    }
}

