package com.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * web启动入口
 */
@MapperScan("com.boot.dao")
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication(scanBasePackages = {"com.boot"})
public class WebBoot3Application {

    private static Logger logger = LoggerFactory.getLogger(WebBoot3Application.class);

    public static void main(String[] args) {
        logger.info("----------WebBoot3Application start-----------");
        SpringApplication application = new SpringApplication(WebBoot3Application.class);
        ConfigurableApplicationContext context = application.run(args);
        logger.info("----------WebBoot3Application run success,"+context.getId());
    }

}
