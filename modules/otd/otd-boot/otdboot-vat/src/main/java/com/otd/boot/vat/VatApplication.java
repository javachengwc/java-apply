package com.otd.boot.vat;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动类
 */
@SpringBootApplication
public class VatApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(VatApplication.class, args);
  }

}
