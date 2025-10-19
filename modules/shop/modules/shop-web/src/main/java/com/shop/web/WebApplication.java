package com.shop.web;

import com.shop.web.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
/**
 * web启动入口
 */
@EnableAutoConfiguration
@SpringBootApplication
public class WebApplication {

    private static Logger logger = LoggerFactory.getLogger(WebApplication.class);

    public static void main(String[] args) {
        logger.info("WebApplication start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { WebApplication.class, Config.class });
        builder.run(args);
        logger.info("WebApplication start success........");
    }

}
