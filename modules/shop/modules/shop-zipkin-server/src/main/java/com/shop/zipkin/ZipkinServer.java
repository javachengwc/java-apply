package com.shop.zipkin;

import com.shop.zipkin.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.EnableZipkinServer;

//rest程序启动入口
@EnableEurekaClient
@SpringBootApplication
@EnableZipkinServer
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ZipkinServer {

    private static Logger logger= LoggerFactory.getLogger(ZipkinServer.class);

    public static void  main(String args []) throws Exception
    {
        logger.info("ZipkinServer start  begin........");

        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { ZipkinServer.class, Config.class }).web(true);
        builder.run(args);
        logger.info("ZipkinServer start success........");
    }
}
