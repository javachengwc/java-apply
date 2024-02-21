package com.otd.boot.web.base.config;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger配置
 * 访问地址：http://localhost:port/swagger-ui/index.html
 */
//@Configuration
//@EnableSwagger2
@Slf4j
public class SwaggerConfig {

  //@Value("${spring.application.name}")
  private String projectName;

  //@Bean
  public Docket createRestApi() {

    log.info("SwaggerConfig createRestApi start");
    ParameterBuilder headerbuilder = new ParameterBuilder();
    headerbuilder.name("token").description("认证所需的token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
    List<Parameter> params = new ArrayList<Parameter>();
    params.add(headerbuilder.build());

    ApiInfo apiInfo = new ApiInfoBuilder().title(projectName)
        .description(projectName + " swagger接口")
        .termsOfServiceUrl("http://localhost")
        .version("1.0").build();
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo)
        .globalOperationParameters(params)
        .select()
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
        .paths(PathSelectors.any()).build();
  }

}

