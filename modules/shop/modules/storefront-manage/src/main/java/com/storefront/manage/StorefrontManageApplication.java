package com.storefront.manage;

import com.storefront.manage.config.Config;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 启动入口
 */
@EnableAutoConfiguration
@SpringBootApplication
public class StorefrontManageApplication {

    private static Logger logger = Logger.getLogger(StorefrontManageApplication.class);

    public static void main(String[] args) {
        logger.info("StorefrontManageApplication start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { StorefrontManageApplication.class, Config.class });
        builder.run(args);
        logger.info("StorefrontManageApplication start success........");
    }

}
