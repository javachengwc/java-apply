package com.cloud.zkapp1;

import com.cloud.zkapp1.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
public class Zk01Application {

    private static Logger logger= LoggerFactory.getLogger(Zk01Application.class);

    public static void  main(String args []) throws Exception
    {
        logger.info("ZkRegistryServer start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(
                new Object[] { Zk01Application.class, Config.class });
        builder.run(args);
        logger.info("ZkRegistryServer start success........");
    }
}
