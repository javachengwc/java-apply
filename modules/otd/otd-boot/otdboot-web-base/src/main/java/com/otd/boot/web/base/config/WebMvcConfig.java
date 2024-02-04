package com.otd.boot.web.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  //允许跨域
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin("*");
    config.setAllowCredentials(true);
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");
    UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
    configSource.registerCorsConfiguration("/**", config);
    return new CorsFilter(configSource);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    // 支持 .do 后缀接口
    configurer.setUseTrailingSlashMatch(true).setUseSuffixPatternMatch(true);
    WebMvcConfigurer.super.configurePathMatch(configurer);
  }

}
