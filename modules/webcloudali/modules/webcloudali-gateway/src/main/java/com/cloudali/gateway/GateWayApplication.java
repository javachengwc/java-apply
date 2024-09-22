package com.cloudali.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.cloudali"},
        exclude= {DataSourceAutoConfiguration.class, AjCaptchaAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.cloudali"})
@EnableDiscoveryClient
public class GateWayApplication {
    public static void main(String[] args){
        SpringApplication springApplication = new SpringApplication(GateWayApplication.class);
        springApplication.run(args);
    }
}
