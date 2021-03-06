package com.socketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketioConfig {

  @Value("${socketio.port}")
  private Integer socketioPort;

  @Bean
  public SocketIOServer socketIOServer() {
    com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
    //config.setHostname("0.0.0.0");
    config.setPort(socketioPort);
    config.setTransports(Transport.WEBSOCKET);
    config.setOrigin("*");
    SocketIOServer server = new SocketIOServer(config);
    return server;
  }

  @Bean
  public SpringAnnotationScanner springAnnotationScanner() {
    return new SpringAnnotationScanner(socketIOServer());
  }

}