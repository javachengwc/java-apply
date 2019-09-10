package com.pseudocode.cloud.zuul;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulProxyMarkerConfiguration {
    @Bean
    public Marker zuulProxyMarkerBean() {
        return new Marker();
    }

    class Marker {

    }
}