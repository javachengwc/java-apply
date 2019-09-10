package com.pseudocode.cloud.eurekaserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EurekaServerMarkerConfiguration
{
    @Bean
    public Marker eurekaServerMarkerBean()
    {
        return new Marker();
    }

    class Marker
    {
        Marker()
        {
        }
    }
}