package com.shop.book.manage.config;

import com.shop.book.manage.filter.RequestReplacedFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(requestReplacedFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("requestReplacedFilter");
        return registration;
    }

    //允许跨域设置
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedHeaders("*")
            .allowedOrigins("*")
            .allowedMethods("*");
    }

    @Bean(name = "requestReplacedFilter")
    public Filter requestReplacedFilter() {
        return new RequestReplacedFilter();
    }

}
