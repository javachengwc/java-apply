package com.sharding.bootdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;

@SpringBootApplication
public class ShardingBootApplication {
  
	private static Logger logger= LoggerFactory.getLogger(ShardingBootApplication.class);

	//Spring boot程序运行入口
	public static void main(String[] args) throws Exception {
		logger.info("ShardingBootApplication start  begin........");
		SpringApplication.run(ShardingBootApplication.class, args);
        logger.info("ShardingBootApplication start success........");
	}
	
}