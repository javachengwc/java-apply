package com.micro.webcore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@ConditionalOnClass({RestTemplate.class})
public class RestInterceptorConfiguration {

    @Autowired(required = false)
    @LoadBalanced
    private Collection<RestTemplate> restTemplates;

    @Autowired(required = false)
    private RestHeaderInterceptor restHeaderInterceptor;

    @PostConstruct
    public void init() {
        if (this.restTemplates != null && this.restHeaderInterceptor != null) {
            for (RestTemplate restTemplate : this.restTemplates) {
                List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>(restTemplate.getInterceptors());
                interceptors.add(this.restHeaderInterceptor);
                restTemplate.setInterceptors(interceptors);
            }
        }
    }
}
