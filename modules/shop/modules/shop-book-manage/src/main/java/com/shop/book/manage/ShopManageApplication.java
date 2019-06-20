package com.shop.book.manage;

import com.shop.book.manage.config.Config;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 启动入口
 */
@SpringBootApplication
public class ShopManageApplication {

    private static Logger logger = Logger.getLogger(ShopManageApplication.class);

    public static void main(String[] args) {
        logger.info("ShopManageApplication start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { ShopManageApplication.class, Config.class });
        builder.run(args);
        logger.info("ShopManageApplication start success........");
    }

}
