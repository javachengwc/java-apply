package com.shop.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger接口文档,
 * 访问地址http://ip:port/swagger-ui.html
 */
@Configuration
@EnableSwagger2
@Profile({"dev"})
public class SwaggerConfig {

	@Bean
	public Docket createRestApi() {
		ApiInfo apiInfo = new ApiInfoBuilder().title("shop-order")
				.description("shop-order swagger接口")
                .termsOfServiceUrl("http://localhost")
                .contact("ccc")
				.version("1.0").build();
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo)
                .select()
				.apis(RequestHandlerSelectors.basePackage("com.shop.order.controller"))
				.paths(PathSelectors.any()).build();
	}

}
