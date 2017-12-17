package com.component.rest.config;

import com.component.rest.interceptor.RestTemplateHeaderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@Profile("microservice")
public class RestHeaderConfig {

    @Autowired(required = false)
    @LoadBalanced
    private Collection<RestTemplate> restTemplates;

    @Autowired(required = false)
    private RestTemplateHeaderInterceptor restTemplateHeaderInterceptor;

    @PostConstruct
    public void init() {
        if (this.restTemplates != null && this.restTemplateHeaderInterceptor != null) {
            for (RestTemplate restTemplate : this.restTemplates) {
                List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>(
                        restTemplate.getInterceptors());
                interceptors.add(this.restTemplateHeaderInterceptor);
                restTemplate.setInterceptors(interceptors);
            }
        }
    }

}
