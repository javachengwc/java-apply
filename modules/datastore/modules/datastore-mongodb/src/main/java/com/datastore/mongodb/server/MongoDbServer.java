package com.datastore.mongodb.server;

import com.datastore.mongodb.server.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

//rest程序启动入口
@EnableAutoConfiguration
@SpringBootApplication
public class MongoDbServer {

    private static Logger logger= LoggerFactory.getLogger(MongoDbServer.class);

    public static void  main(String args []) throws Exception
    {
        logger.info("MongoDbServer start  begin........");

        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { MongoDbServer.class, Config.class }).web(true);
        builder.run(args);
        logger.info("MongoDbServer start success........");
    }
}
