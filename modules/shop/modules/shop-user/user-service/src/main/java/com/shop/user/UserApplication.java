package com.shop.user;

import com.shop.user.config.Config;
import org.apache.log4j.Logger;
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
public class UserApplication {

    private static Logger logger = Logger.getLogger(UserApplication.class);

    public static void main(String[] args) {
        logger.info("UserApplication start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { UserApplication.class, Config.class });
        builder.run(args);
        logger.info("UserApplication start success........");
    }

}
