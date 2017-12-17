package com.component.rest.config;

import com.component.rest.balance.LoadBalanceRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfig {

    @Bean
    @Autowired
    public IRule getRibbonRule() {
        return new LoadBalanceRule();
    }
}
