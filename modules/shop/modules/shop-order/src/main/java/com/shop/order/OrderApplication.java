package com.shop.order;

import com.shop.order.config.Config;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 启动入口
 */
@EnableAutoConfiguration
@SpringBootApplication
public class OrderApplication {

    private static Logger logger = Logger.getLogger(OrderApplication.class);

    public static void main(String[] args) {
        logger.info("OrderApplication start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { OrderApplication.class, Config.class });
        builder.run(args);
        logger.info("OrderApplication start success........");
    }

}
