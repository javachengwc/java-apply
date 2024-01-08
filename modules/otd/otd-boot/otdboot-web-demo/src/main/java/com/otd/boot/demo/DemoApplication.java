package com.otd.boot.demo;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动类
 */
@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(DemoApplication.class, args);
  }

}
