package com.otd.boot.demo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig  implements WebMvcConfigurer {

    //编码过滤器
    @Bean
    public FilterRegistrationBean encodingFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    //添加静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }
}
