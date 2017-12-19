package com.cloud.feign;

import com.cloud.feign.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

//rest程序启动入口
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class FeignServer {

    private static Logger logger= LoggerFactory.getLogger(FeignServer.class);

    public static void  main(String args []) throws Exception
    {
        logger.info("FeignServer start  begin........");

        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { FeignServer.class, Config.class }).web(true);
        builder.run(args);
//        SpringApplication.run(FeignServer.class, args);
        logger.info("FeignServer start success........");
    }
}
