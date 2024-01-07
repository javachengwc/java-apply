package com.otd.boot.web.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * WebBase配置
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = { "com.otd.boot.web.base" })
public class WebBaseConfig implements EnvironmentAware {

  private static String[] activeProfiles = null;

  private static Environment environment;

  @Override
  public void setEnvironment(Environment env) {
    environment = env;
    activeProfiles = env.getActiveProfiles();
    log.info("----------------------WebBaseConfig Application profile={}--------------------", activeProfiles);
  }

  public static String[] getActiveProfiles() {
    return activeProfiles;
  }

  public static Environment getEnvironment() {
    return environment;
  }

  @Bean
  public BeanPostProcessor beanPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessBeforeInitialization(Object o, String s) {
        log.info("WebBaseConfig beanPostProcessor object:" + o.getClass().getSimpleName());
        return o;
      }

      @Override
      public Object postProcessAfterInitialization(Object o, String s) {
        return o;
      }
    };
  }

}
