package com.cloud.gateway.server;

import com.cloud.gateway.filter.AccessFilter;
import com.cloud.gateway.server.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

//rest程序启动入口
@EnableAutoConfiguration
@EnableZuulProxy
@SpringCloudApplication
public class GatewayServer {

    private static Logger logger= LoggerFactory.getLogger(GatewayServer.class);

    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }

    public static void  main(String args []) throws Exception
    {
        logger.info("GatewayServer start  begin........");

        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { GatewayServer.class, Config.class }).web(true);
        builder.run(args);
        logger.info("GatewayServer start success........");
    }
}
