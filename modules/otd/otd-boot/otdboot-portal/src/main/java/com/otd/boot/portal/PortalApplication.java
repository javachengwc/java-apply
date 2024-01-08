package com.otd.boot.portal;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动类
 */
@SpringBootApplication
public class PortalApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(PortalApplication.class, args);
  }

}
