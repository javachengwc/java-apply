package com.micro.basic;

import com.micro.webcore.ApplicationStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class BasicApplication {

    private final static Logger logger = LoggerFactory.getLogger(BasicApplication.class);

    public static void main(String[] args) {
        ApplicationStarter.run(BasicApplication.class, args);
        logger.info("BasicApplication start success......................");
    }
}
