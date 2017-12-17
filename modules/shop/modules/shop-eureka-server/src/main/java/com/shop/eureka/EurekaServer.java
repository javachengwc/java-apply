package com.shop.eureka;

import com.shop.eureka.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//rest程序启动入口
@EnableAutoConfiguration
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer {

    private static Logger logger= LoggerFactory.getLogger(EurekaServer.class);

    public static void  main(String args []) throws Exception
    {
        logger.info("EurekaServer start  begin........");

        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { EurekaServer.class, Config.class }).web(true);
        builder.run(args);
        logger.info("EurekaServer start success........");
    }
}
