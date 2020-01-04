package com.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.socketio.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocketioApplication {

  private static Logger logger= LoggerFactory.getLogger(SocketioApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(SocketioApplication.class, args);

    SocketIOServer socketIOServer = SpringContextUtil.getBean(SocketIOServer.class);
    socketIOServer.start();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          socketIOServer.stop();
          logger.info("socketIOServer stop");
        } catch (Exception e) {
          logger.error("socketIOServer stop error,", e);
        }
      }
    });
  }

}
