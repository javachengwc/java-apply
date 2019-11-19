package com.micro.apollo.pseudocode.configservice.apollo.configservice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEurekaClient
@ConditionalOnProperty(name = "apollo.eureka.client.enabled", havingValue = "true", matchIfMissing = true)
public class ConfigServerEurekaClientConfigure {
}

