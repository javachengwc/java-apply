package com.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GrpcClientApplication {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(GrpcClientApplication.class);
        logger.info("GrpcClientApplication start...............");
        SpringApplication application = new SpringApplication(GrpcClientApplication.class);
        logger.info("GrpcClientApplication run start...............");
        ConfigurableApplicationContext context = application.run(args);
        logger.info("GrpcClientApplication run success, contextId={}",context.getId());
    }
}
