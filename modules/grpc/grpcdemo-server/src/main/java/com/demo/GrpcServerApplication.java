package com.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GrpcServerApplication {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(GrpcServerApplication.class);
        logger.info("GrpcServerApplication start...............");
        SpringApplication application = new SpringApplication(GrpcServerApplication.class);
        logger.info("GrpcServerApplication run start...............");
        ConfigurableApplicationContext context = application.run(args);
        logger.info("GrpcServerApplication run success, contextId={}",context.getId());
    }
}
