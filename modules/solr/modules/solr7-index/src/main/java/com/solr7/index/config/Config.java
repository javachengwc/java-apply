package com.solr7.index.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
	
	private static Logger logger = LoggerFactory.getLogger(Config.class);

	@Bean(name="restTemplate")
	public RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(3000); //连接超时时间
		requestFactory.setReadTimeout(3000);  //请求超时时间
		return new RestTemplate(requestFactory);
	}

    @Bean
    public BeanPostProcessor beanPostProcessor() {
	    return new BeanPostProcessor() {
	        public Object postProcessBeforeInitialization(Object o, String s) {
	            logger.info("BeanPostProcessor object:" + o.getClass().getSimpleName());
	            return o;
	        }
	        public Object postProcessAfterInitialization(Object o, String s) {
	            return o;
	        }
	    };
	}
	
}
