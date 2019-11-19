package com.micro.apollo.pseudocode.configservice.apollo.configservice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@Configuration
@EnableEurekaServer
@ConditionalOnProperty(name = "apollo.eureka.server.enabled", havingValue = "true", matchIfMissing = true)
public class ConfigServerEurekaServerConfigure {
}

