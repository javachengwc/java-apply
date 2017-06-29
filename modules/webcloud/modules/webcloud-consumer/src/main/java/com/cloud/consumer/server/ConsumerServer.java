package com.cloud.consumer.server;

import com.cloud.consumer.server.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//rest程序启动入口
@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class ConsumerServer {

    private static Logger logger= LoggerFactory.getLogger(ConsumerServer.class);

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void  main(String args []) throws Exception
    {
        logger.info("ConsumerServer start  begin........");

        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { ConsumerServer.class, Config.class }).web(true);
        builder.run(args);
        logger.info("ConsumerServer start success........");
    }
}
