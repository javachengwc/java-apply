package com.solr7.index.config;

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

@Configuration
@EnableSwagger2
@Profile({"dev","test"})
public class Swagger2Config {

	@Bean
	public Docket createRestApi() {
		ApiInfo apiInfo = new ApiInfoBuilder().title("solr7 index api")
				.description("solr7 index api")
				.termsOfServiceUrl("http://www.ccc.com").contact("mi")
				.version("1.1").build();
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo).select()
				.apis(RequestHandlerSelectors.basePackage("com.solr7.index.web"))
				.paths(PathSelectors.any()).build();
	}

}
