package com.springdubbo.dubbo;

import com.springdubbo.dubbo.config.BootConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 启动入口
 */
@Slf4j
public class DubboStarter {

  private static volatile ConfigurableApplicationContext instance;

  public static void run(Class<?> assemblyClass, String[] args) {
    if (instance != null) {
      log.warn("DubboStarter instance exists");
      return;
    }
    synchronized (DubboStarter.class) {
      if (instance == null) {
        log.info("DubboStarter run start");
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DubboStarter.class,assemblyClass).sources(BootConfig.class);
        instance = (ConfigurableApplicationContext)builder.run(args);
        instance.registerShutdownHook();
      } else {
        log.warn("DubboStarter instance exists");
      }
    }
  }
}

