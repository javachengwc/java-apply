package com.otd.boot.wms;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动类
 */
@SpringBootApplication
public class WmsApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(WmsApplication.class, args);
  }

}
