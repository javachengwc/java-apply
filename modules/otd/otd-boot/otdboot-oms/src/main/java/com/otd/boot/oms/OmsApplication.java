package com.otd.boot.oms;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动类
 */
@SpringBootApplication
public class OmsApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(OmsApplication.class, args);
  }

}
