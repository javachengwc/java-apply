package com.spring.pseudocode.webmvc.config.annotation;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

//配置web方面的组件，比如说消息转化，拦截器，视图转化器，跨域配置，资源控制器等等
//类似以前用xml配置时候的spring-web.xml
public abstract interface WebMvcConfigurer {

  default void addCorsMappings(CorsRegistry registry) {
  }

  default void addInterceptors(InterceptorRegistry registry) {
  }
}
