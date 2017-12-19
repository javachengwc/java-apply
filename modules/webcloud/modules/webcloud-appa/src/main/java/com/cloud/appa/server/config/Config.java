package com.cloud.appa.server.config;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = { "com.cloud.appa" })
public class Config {

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            public Object postProcessBeforeInitialization(Object o, String s) {

                System.out.println("BeanPostProcessor object:"+o.getClass().getSimpleName());
                return o;
            }

            public Object postProcessAfterInitialization(Object o, String s) {
                return o;
            }
        };
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
