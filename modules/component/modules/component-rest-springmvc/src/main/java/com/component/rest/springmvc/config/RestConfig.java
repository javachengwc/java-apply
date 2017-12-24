package com.component.rest.springmvc.config;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
@ComponentScan(basePackages = { "com.component.rest.springmvc" })
@RibbonClients(defaultConfiguration={RibbonConfig.class})
public class RestConfig {

}
