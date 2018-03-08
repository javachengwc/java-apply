package com.solr7.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication(exclude=SolrAutoConfiguration.class)
public class SearchApplication {
  
	private static Logger logger= LoggerFactory.getLogger(SearchApplication.class);

	//支持跨域
	@Configuration
	public class CorsConfig extends WebMvcConfigurerAdapter {
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**").allowedOrigins("*");
		}
	}
	
	//Spring boot程序运行入口
	public static void main(String[] args) throws Exception {
		logger.info("SearchApplication start  begin........");
		SpringApplication.run(SearchApplication.class, args);
        logger.info("SearchApplication start success........");
	}
	
}