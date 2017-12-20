package com.cloud.zkapp2;

import com.cloud.zkapp2.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
public class Zk02Application {

    private static Logger logger= LoggerFactory.getLogger(Zk02Application.class);

    public static void  main(String args []) throws Exception
    {
        logger.info("Zk02Application start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(
                new Object[] { Zk02Application.class, Config.class });
        builder.run(args);
        logger.info("Zk02Application start success........");
    }
}
