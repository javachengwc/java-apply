package com.solr7.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude=SolrAutoConfiguration.class)
public class IndexApplication {
  
	private static Logger logger= LoggerFactory.getLogger(IndexApplication.class);

	public static void main(String[] args) throws Exception {
		logger.info("IndexApplication start  begin........");
		SpringApplication.run(IndexApplication.class, args);
        logger.info("IndexApplication start success........");
	}
	
}