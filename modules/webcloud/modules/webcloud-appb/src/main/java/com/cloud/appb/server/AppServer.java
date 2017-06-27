package com.cloud.appb.server;

import com.cloud.appb.server.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//rest程序启动入口
@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
public class AppServer {

    private static Logger logger= LoggerFactory.getLogger(AppServer.class);

    public static void  main(String args []) throws Exception
    {
        logger.info("AppServer start  begin........");
        //SpringApplication.run(AppServer.class, args);

        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { AppServer.class, Config.class }).web(true);
        builder.run(args);
        logger.info("AppServer start success........");
    }
}
