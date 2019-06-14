package com.micro.training;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableApolloConfig
public class TrainingApplication {

    public static void main(String[] args) {
        System.setProperty("env","dev");
        SpringApplication.run(TrainingApplication.class, args);
    }

}
