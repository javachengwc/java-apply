package com.shop.order.config;

import com.component.rest.springmvc.ResourceFactory;
import com.shop.user.api.rest.UserResCtrl;
import com.shop.user.api.rest.UserResource;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = { "com.shop.order" })
@MapperScan("com.shop.order.dao")
public class Config {
	
	private static Logger logger = LoggerFactory.getLogger(Config.class);

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000); //连接超时时间
        requestFactory.setReadTimeout(1000);  //请求超时时间
        return new RestTemplate(requestFactory);
    }
	
    @Bean
    public BeanPostProcessor beanPostProcessor() {
	    return new BeanPostProcessor() {
	        @Override
	        public Object postProcessBeforeInitialization(Object o, String s) {
	            logger.info("BeanPostProcessor object:" + o.getClass().getSimpleName());
	            return o;
	        }
	
	        @Override
	        public Object postProcessAfterInitialization(Object o, String s) {
	            return o;
	        }
	    };
	}

    @Bean
    @Autowired
    public UserResCtrl userResCtrl(ResourceFactory resourceFactory) {
        return resourceFactory.getSpringMvcResource(UserResCtrl.class,restTemplate());
    }
}
