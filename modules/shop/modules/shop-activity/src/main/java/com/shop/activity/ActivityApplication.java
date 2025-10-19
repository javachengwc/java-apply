package com.shop.activity;

import com.shop.activity.config.Config;
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
public class ActivityApplication {

    private static Logger logger = LoggerFactory.getLogger(ActivityApplication.class);

    public static void main(String[] args) {
        logger.info("ActivityApplication start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { ActivityApplication.class, Config.class });
        builder.run(args);
        logger.info("ActivityApplication start success........");
    }

}
