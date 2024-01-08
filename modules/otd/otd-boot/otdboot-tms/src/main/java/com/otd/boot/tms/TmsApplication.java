package com.otd.boot.tms;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动类
 */
@SpringBootApplication
public class TmsApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(TmsApplication.class, args);
  }

}
