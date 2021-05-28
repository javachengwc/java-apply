package com.manage.rbac.autoconfigure;

import com.manage.rbac.context.LoginContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RbacAutoConfiguration {

    @Bean
    public  LoginContext loginContext() {
        return new LoginContext();
    }
}
