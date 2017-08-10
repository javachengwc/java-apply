package com.app.server;

import com.app.server.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

//rest程序启动入口
@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class RestServer {

    private static Logger logger= LoggerFactory.getLogger(RestServer.class);

    public static void  main(String args []) throws Exception
    {
        logger.info("RestServer start  begin........");
        //SpringApplication.run(RestServer.class, args);

        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { RestServer.class, Config.class });
        //builder.sources(MainConfig.class);
        //builder.profiles("a","b");
        builder.run(args);
        logger.info("RestServer start success........");
    }
}
