package com.otd.boot.demo;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动类
 */
@SpringBootApplication
public class DemoApiApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(DemoApiApplication.class, args);
  }

}
