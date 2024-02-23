package com.otd.boot.web.base.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * web配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  //允许跨域
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");
    config.setAllowedOriginPatterns(Lists.newArrayList("*"));
    UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
    configSource.registerCorsConfiguration("/**", config);
    return new CorsFilter(configSource);
  }

//  跨域或者
//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/**")             // 允许跨域访问的路径
//            .allowedOriginPatterns("*")    // 允许跨域访问的源
//  }

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

//    UrlPathHelper urlPathHelper = new UrlPathHelper();
//    configurer.setUrlPathHelper(urlPathHelper);
    // 这里设置没启作用，还是需要在  application.properties/yml文件中设置
    // spring.mvc.pathmatch.matching-strategy=ant_path_matcher才生效
    AntPathMatcher antPathMatcher =new AntPathMatcher();
    configurer.setPathMatcher(antPathMatcher);

  }

}
