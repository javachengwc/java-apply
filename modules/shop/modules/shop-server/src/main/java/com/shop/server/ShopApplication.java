package com.shop.server;

import com.shop.server.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 启动入口
 */
@EnableAutoConfiguration
@SpringBootApplication
public class ShopApplication {

    private static Logger logger = LoggerFactory.getLogger(ShopApplication.class);

    public static void main(String[] args) {
        logger.info("ShopApplication start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { ShopApplication.class, Config.class });
        builder.run(args);
        logger.info("ShopApplication start success........");
    }

}
