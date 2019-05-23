package com.micro.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger接口文档
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket createRestApi() {
		ApiInfo apiInfo = new ApiInfoBuilder().title("micro-course")
				.description("micro-course swagger接口")
                .termsOfServiceUrl("http://localhost")
                .contact("ccc")
				.version("1.0").build();
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo)
                .select()
				.apis(RequestHandlerSelectors.basePackage("com.micro.course.controller"))
				.paths(PathSelectors.any()).build();
	}

}
