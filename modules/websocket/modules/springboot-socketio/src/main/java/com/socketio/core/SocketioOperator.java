package com.socketio.core;

import com.corundumstudio.socketio.SocketIOClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketioOperator {

  private static Logger logger = LoggerFactory.getLogger(SocketioOperator.class);

  public static void sendEventInfo(SocketIOClient client,String event,String info) {
    String sessionId = client.getSessionId().toString();
    try{
      if(!client.isChannelOpen()){
        logger.info("SocketioOperator sendEventInfo client channel is not open,sessionId={}",sessionId);
      } else {
        logger.info("SocketioOperator sendEventInfo info={},sessionId={}",info,sessionId);
        client.sendEvent(event,info);
      }
    }catch (Exception e) {
      logger.error("SocketioOperator sendEventInfo error,sessionId={},event={},",sessionId,event,e);
    }
  }

  public static void closeClient(SocketIOClient client) {
    String sessionId = client.getSessionId().toString();
    logger.info("SocketioOperator closeClient start,sessionId={}",sessionId);
    try{
      if(client!=null && client.isChannelOpen()) {
        client.disconnect();
      }
    }catch (Exception e) {
      logger.error("SocketioOperator closeClient error,sessionId={}",sessionId,e);
    }
  }

}