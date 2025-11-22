package com.boot3.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ApplicationConfig {

    @Value("${spring.profiles.active:}")
    private String profile;

    @Value("${spring.application.name:}")
    private String appName;

    @Value("${server.port:}")
    private String port;

}
