package com.otd.boot.demo;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 程序启动类
 */
@ImportResource(locations = {"classpath:applicationContext.xml","classpath:spring-mvc.xml"})
@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(DemoApplication.class, args);
  }

}
