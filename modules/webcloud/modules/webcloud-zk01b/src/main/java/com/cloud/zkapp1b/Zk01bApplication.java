package com.cloud.zkapp1b;

import com.cloud.zkapp1b.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
public class Zk01bApplication {

    private static Logger logger= LoggerFactory.getLogger(Zk01bApplication.class);

    public static void  main(String args []) throws Exception
    {
        logger.info("Zk01bApplication start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(
                new Object[] { Zk01bApplication.class, Config.class });
        builder.run(args);
        logger.info("Zk01bApplication start success........");
    }
}
