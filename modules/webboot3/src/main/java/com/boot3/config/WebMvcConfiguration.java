package com.boot3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //添加拦截器
        AuthInterceptor authInterceptor = new AuthInterceptor();
        InterceptorRegistration authRegtion = registry.addInterceptor(authInterceptor);
        authRegtion.addPathPatterns("/**");
        List<String> exclude = new ArrayList<>();
        exclude.add("/do/**");
        authRegtion.excludePathPatterns(exclude);

        //添加跨域拦截器
        String[] corsDomains = new String[0];
        CsrfInterceptor csrfInterceptor = new CsrfInterceptor(corsDomains);
        InterceptorRegistration interceptor = registry.addInterceptor(csrfInterceptor);
        interceptor.addPathPatterns("/**");
        interceptor.excludePathPatterns(exclude);

    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}