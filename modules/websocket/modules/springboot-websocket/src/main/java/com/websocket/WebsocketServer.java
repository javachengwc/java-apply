package com.websocket;

import com.websocket.service.WebsocketSession;
import com.websocket.service.WebsocketSessionFactory;
import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;


@ServerEndpoint("/ws")
@Component
public class WebsocketServer {

  //建立连接成功调用
  @OnOpen
  public void onOpen(Session session){
    WebsocketSessionFactory.addSession(session);
    System.out.println("WebSocketServer onOpen......");
  }

  //连接关闭调用
  @OnClose
  public void onClose(Session session){
    WebsocketSessionFactory.closeSession(session);
    System.out.println("WebSocketServer onClose......");
  }

  //收到消息后调用
  @OnMessage
  public void onMessage(String message, Session session) {
    System.out.println("WebSocketServer onMessage invoked,message=" +message);
    for(WebsocketSession per: WebsocketSessionFactory.getSessionSet()){
      try {
        per.sendMessage(message);
      } catch (IOException e) {
        e.printStackTrace(System.out);
        continue;
      }
    }
  }

  @OnError
  public void onError(Session session, Throwable error){
    System.out.println("WebSocketServer onError......");
    error.printStackTrace(System.out);
  }

}
