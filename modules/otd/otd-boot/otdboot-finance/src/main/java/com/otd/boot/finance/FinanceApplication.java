package com.otd.boot.finance;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动类
 */
@SpringBootApplication
public class FinanceApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(FinanceApplication.class, args);
  }

}
