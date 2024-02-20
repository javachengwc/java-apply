package com.otd.boot.oms;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 程序启动类
 */
@ImportResource(locations = {"classpath:applicationContext.xml"})
@SpringBootApplication
@EnableTransactionManagement
public class OmsApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(OmsApplication.class, args);
  }

}
