package com.springdubbo;

import com.springdubbo.config.BootConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 启动入口
 */
@Slf4j
public class ApplicationStarter {

  private static volatile ConfigurableApplicationContext instance;

  public static void run(Class<?> assemblyClass, String[] args) {
    if (instance != null) {
      log.warn("ApplicationStarter instance exists");
      return;
    }
    synchronized (ApplicationStarter.class) {
      if (instance == null) {
        log.info("ApplicationStarter run start");
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ApplicationStarter.class,assemblyClass).sources(BootConfig.class);
        instance = (ConfigurableApplicationContext)builder.run(args);
        instance.registerShutdownHook();
      } else {
        log.warn("ApplicationStarter instance exists");
      }
    }
  }
}
