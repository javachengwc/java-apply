package com.shop.book;

import com.shop.book.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动入口
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class BookApplication {

    private static Logger logger = LoggerFactory.getLogger(BookApplication.class);

    public static void main(String[] args) {
        logger.info("BookApplication start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { BookApplication.class, Config.class });
        builder.run(args);
        logger.info("BookApplication start success........");
    }

}
