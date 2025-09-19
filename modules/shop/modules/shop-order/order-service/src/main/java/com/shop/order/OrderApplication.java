package com.shop.order;

import com.component.rest.springmvc.ApplicationStarter;
import com.shop.order.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动入口
 */
@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
public class OrderApplication {

    private static Logger logger = LoggerFactory.getLogger(OrderApplication.class);

    public static void main(String[] args) {
        logger.info("OrderApplication start  begin........");
//        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { OrderApplication.class, Config.class });
//        builder.run(args);
        ApplicationStarter.run(Config.class, args);
        logger.info("OrderApplication start success........");
    }

}
