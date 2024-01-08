package com.otd.boot.report;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动类
 */
@SpringBootApplication
public class ReportApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(ReportApplication.class, args);
  }

}
