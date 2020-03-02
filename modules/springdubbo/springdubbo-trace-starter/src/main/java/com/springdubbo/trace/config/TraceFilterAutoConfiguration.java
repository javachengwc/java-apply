package com.springdubbo.trace.config;

import com.springdubbo.trace.filter.WebTraceIDFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TraceFilterAutoConfiguration {

    @Bean
    @ConditionalOnWebApplication
    WebTraceIDFilter webTraceIDFilter() {
        return new WebTraceIDFilter();
    }
}
