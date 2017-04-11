package com.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * 读取properties配置文件,之后可直接用 @Value("${name}")注解来取配置的值
 */
@Configuration
public class PropertiesConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer createPropertySourcesPlaceholderConfigurer() {
        ClassPathResource jdbcResource = new ClassPathResource("jdbc.properties");
        ClassPathResource varResource = new ClassPathResource("var.properties");
        PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocations(jdbcResource,varResource);
        return propertyPlaceholderConfigurer;
    }
}
