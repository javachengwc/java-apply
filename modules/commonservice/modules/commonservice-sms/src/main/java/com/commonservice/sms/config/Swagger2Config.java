package com.commonservice.sms.config;

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
@Profile({"dev","test"})
public class Swagger2Config {

    //生成Docket对象,对API的包进行扫描
    public Docket createRestApi() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("公共服务SMS").description("公共服务SMS接口文档").version("1.0.0").build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo).select()
                .apis(RequestHandlerSelectors.basePackage("com.commonservice.sms.controller"))
                .paths(PathSelectors.any()).build();
    }
}
