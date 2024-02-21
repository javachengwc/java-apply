package com.otd.boot.demo.es;

import com.otd.boot.web.base.ApplicationStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * 程序启动类
 */
@ImportResource(locations = {"classpath:applicationContext.xml"})
@SpringBootApplication(exclude={ElasticsearchDataAutoConfiguration.class})
public class EsApplication {

  public static void main(String[] args) {
    ApplicationStarter.run(EsApplication.class, args);
  }

}
