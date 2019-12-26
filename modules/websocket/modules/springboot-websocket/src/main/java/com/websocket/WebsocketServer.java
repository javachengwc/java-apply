package com.websocket;

import com.websocket.service.WebsocketSession;
import com.websocket.service.WebsocketSessionFactory;
import java.io.IOException;

import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@ServerEndpoint("/ws")
@Component
public class WebsocketServer {

  //建立连接成功调用
  @OnOpen
  public void onOpen(Session session){
    String sessionId =session.getId();
    System.out.println("WebSocketServer onOpen,sessionId="+sessionId);
    String name="";

    //name=cheng
    String queryStr = session.getQueryString();
    System.out.println("session.getQueryString():"+queryStr);
    if(!StringUtils.isBlank(queryStr)) {
      name = queryStr.replace("name=","");
    }

    WebsocketSessionFactory.addSession(session,name);

    //@ServerEndpoint("/ws/{param}")
    //public void open(Session session, @PathParam("param")String  param)
    //System.out.println("用户"+param+" 登录");
    Map<String, String> map = session.getPathParameters();
    System.out.println("session.getPathParameters():"+map);

    String uri = session.getRequestURI().toString();
    System.out.println("session.getRequestURI():"+uri);
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
    for(WebsocketSession per: WebsocketSessionFactory.getSessionCollection()){
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
