package com.websocket.service;

import java.io.IOException;
import javax.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionOperator {

  private static Logger logger = LoggerFactory.getLogger(SessionOperator.class);

  public static void sendMessage(Session session,String content) {
    String sessionId = session.getId();
    try{
      if(!session.isOpen()){
        logger.info("SessionOperator session is close,sessionId={}",sessionId);
      } else {
        session.getBasicRemote().sendText(content);
      }
    }catch (IOException e) {
      logger.error("SessionOperator sendMessage error,sessionId={}",sessionId,e);
    }
  }

  public static void closeSession(Session session) {
    String sessionId = session==null?"":session.getId();
    logger.info("SessionOperator closeSession start,sessionId={}",sessionId);
    try{
      if(session!=null) {
        session.close();
      }
    }catch (IOException e) {
      logger.error("SessionOperator closeSession error,sessionId={}",sessionId,e);
    }
  }

}
