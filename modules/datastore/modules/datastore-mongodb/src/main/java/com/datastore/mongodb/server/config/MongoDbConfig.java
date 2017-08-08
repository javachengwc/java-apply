package com.datastore.mongodb.server.config;

import com.datastore.mongodb.server.MongoDbProperties;
import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class MongoDbConfig {

    private static Logger logger = LoggerFactory.getLogger(MongoDbConfig.class);

    @Bean
    public DBCollection mongoDbCollection(MongoDbProperties prop) {
        String database =prop.getDatabase();
        String collection =prop.getCollection();
        String ip=prop.getIp();
        Integer port =prop.getPort();
        logger.info("MongoDbConfig mongoDbCollection start ....ip={},port={},database={},collection={}",ip,port,database,collection);
        //mongo地址
        ServerAddress address = new ServerAddress(ip,port);
        //认证账号
        List<MongoCredential> credentials = Arrays.asList(MongoCredential.createScramSha1Credential(
                prop.getUserName(),database,prop.getPassword().toCharArray()));
        //选项
        MongoClientOptions options = MongoClientOptions.builder().maxWaitTime(prop.getMaxWaitTime()).connectTimeout(prop.getConnectTimeout()).build();
        MongoClient mongoClient =new MongoClient(address,credentials, options);

        DBCollection dbCollection= mongoClient.getDB(database).getCollection(collection);
        logger.info("MongoDbConfig mongoDbCollection success ....");
        return dbCollection;
    }

}
