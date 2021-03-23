package com.commonservice.invoke.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class Config implements EnvironmentAware {

    private static String [] activeProfiles= null;

    @Override
    public void setEnvironment(Environment environment) {
        activeProfiles = environment.getActiveProfiles();
        log.info("===========================Application profile={}==============================", activeProfiles);
    }

    public static String [] getActiveProfiles() {
        return activeProfiles;
    }

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object o, String s) {
                log.info("BeanPostProcessor object:" + o.getClass().getSimpleName());
                return o;
            }

            @Override
            public Object postProcessAfterInitialization(Object o, String s) {
                return o;
            }
        };
    }

    @Bean(name="restTemplate")
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000); //连接超时时间
        requestFactory.setReadTimeout(3000);  //请求超时时间
        return new RestTemplate(requestFactory);
    }
}