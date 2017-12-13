package com.app.server.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiConfig() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("controller")
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.app.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket restConfig() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("jersey")
                .apiInfo(restInfo())
                .forCodeGeneration(true)
                .pathMapping("/").select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("controller api")// 大标题
                .description("spring boot controller api文档")// 小标题
                .version("2.0").build();
    }

    private ApiInfo restInfo() {
        return new ApiInfoBuilder().title("rest api")// 大标题
                .description("spring boot rest api文档")// 小标题
                .version("2.0").build();
    }
}
