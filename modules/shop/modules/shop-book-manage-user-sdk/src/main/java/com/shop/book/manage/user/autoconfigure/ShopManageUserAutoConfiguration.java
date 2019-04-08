package com.shop.book.manage.user.autoconfigure;

import com.shop.book.manage.user.interceptor.ShopManageInterceptor;
import com.shop.book.manage.user.service.ShopManageUserService;
import com.shop.book.manage.user.service.impl.ShopManageUserImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
@EnableConfigurationProperties(ShopManageUserProperties.class)
@ConditionalOnClass({ShopManageUserService.class})
public class ShopManageUserAutoConfiguration {

    @Bean
    public ShopManageUserService shopManageUserService() {
        return new ShopManageUserImpl();
    }

    @Bean
    public HandlerInterceptor shopManageInterceptor() {
        return new ShopManageInterceptor();
    }
}
