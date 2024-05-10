package com.sharding.junior;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShardingJuniorApplication {
  
	private static Logger logger= LoggerFactory.getLogger(ShardingJuniorApplication.class);

	//Spring boot程序运行入口
	public static void main(String[] args) {
		logger.info("ShardingJuniorApplication start  begin........");
		SpringApplication.run(ShardingJuniorApplication.class, args);
        logger.info("ShardingJuniorApplication start success........");
	}
	
}