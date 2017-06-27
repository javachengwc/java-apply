package com.cloud.center.server;

import com.cloud.center.server.config.Config;
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
public class CloudServer {

    private static Logger logger= LoggerFactory.getLogger(CloudServer.class);

    public static void  main(String args []) throws Exception
    {
        logger.info("CloudServer start  begin........");
        //SpringApplication.run(CloudServer.class, args);

        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { CloudServer.class, Config.class }).web(true);
        builder.run(args);
        logger.info("CloudServer start success........");
    }
}
