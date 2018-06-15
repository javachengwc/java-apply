package com.esearch6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;

@SpringBootApplication(exclude=ElasticsearchAutoConfiguration.class)
public class ESearchApplication {
  
	private static Logger logger= LoggerFactory.getLogger(ESearchApplication.class);

	//Spring boot程序运行入口
	public static void main(String[] args) throws Exception {
		logger.info("ESearchApplication start  begin........");
		SpringApplication.run(ESearchApplication.class, args);
        logger.info("ESearchApplication start success........");
	}
	
}