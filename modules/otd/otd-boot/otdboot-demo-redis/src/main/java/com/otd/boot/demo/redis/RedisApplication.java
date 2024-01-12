package com.otd.boot.demo.redis;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 程序启动类
 */
@ImportResource(locations = {"classpath:applicationContext.xml"})
@SpringBootApplication
public class RedisApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(RedisApplication.class, args);
  }

}
