package com.storefront.manage;

import com.storefront.manage.config.Config;
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
public class StorefrontManageApplication {

    private static Logger logger = LoggerFactory.getLogger(StorefrontManageApplication.class);

    public static void main(String[] args) {
        logger.info("StorefrontManageApplication start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { StorefrontManageApplication.class, Config.class });
        builder.run(args);
        logger.info("StorefrontManageApplication start success........");
    }

}
