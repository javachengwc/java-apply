package com.pseudocode.cloud.eurekaclient.ribbon;

import com.pseudocode.cloud.ribbon.RibbonAutoConfiguration;
import com.pseudocode.cloud.ribbon.RibbonClients;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
//@ConditionalOnRibbonAndEurekaEnabled
@AutoConfigureAfter(RibbonAutoConfiguration.class)
@RibbonClients(defaultConfiguration = EurekaRibbonClientConfiguration.class)
public class RibbonEurekaAutoConfiguration {


}