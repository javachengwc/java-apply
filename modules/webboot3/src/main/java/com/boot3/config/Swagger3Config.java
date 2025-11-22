package com.boot3.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 访问地址
 * http://ip:port/swagger-ui/index.html
 */
@Configuration
public class Swagger3Config {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API文档")
                        .version("1.0.0")
                        .description("API文档"));
    }

}

