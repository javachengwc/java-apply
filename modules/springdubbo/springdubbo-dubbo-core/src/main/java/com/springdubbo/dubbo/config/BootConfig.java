package com.springdubbo.dubbo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackages = {"com.springdubbo.dubbo"})
@Slf4j
public class BootConfig implements EnvironmentAware {

  private static String[] activeProfiles = null;

  private static Environment environment;

  @Override
  public void setEnvironment(Environment env) {
    environment = env;
    activeProfiles = env.getActiveProfiles();
    log.info("----------------------BootConfig Application profile={}--------------------", activeProfiles);
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
        log.info("BeanPostProcessor object:" + o.getClass().getSimpleName());
        return o;
      }

      @Override
      public Object postProcessAfterInitialization(Object o, String s) {
        return o;
      }
    };
  }
}

