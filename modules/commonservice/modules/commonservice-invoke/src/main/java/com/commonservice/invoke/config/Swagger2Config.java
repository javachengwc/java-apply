package com.commonservice.invoke.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    public Docket createRestApi() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("公共服务INVOKE")
            .description("公共服务INVOKE接口文档")
            .version("1.0.0").build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo).select()
                .apis(RequestHandlerSelectors.basePackage("com.commonservice.invoke.controller"))
                .paths(PathSelectors.any()).build();
    }
}
