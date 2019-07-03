package com.micro.user.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = "project", name = "swagger-open", havingValue = "true")
public class SwaggerConfig {

	@Bean
	public Docket createRestApi() {
		ApiInfo apiInfo = new ApiInfoBuilder().title("micro-user")
				.description("micro-user swagger接口")
                .termsOfServiceUrl("http://localhost")
                .contact("ccc")
				.version("1.0").build();
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo)
                .select()
				.apis(RequestHandlerSelectors.basePackage("com.micro.user.controller"))
				.paths(PathSelectors.any()).build();
	}

}
