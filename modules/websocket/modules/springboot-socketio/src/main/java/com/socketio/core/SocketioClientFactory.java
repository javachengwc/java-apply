package com.socketio.core;

import com.corundumstudio.socketio.SocketIOClient;
import com.socketio.core.runner.PtyRunnerFactory;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketioClientFactory {

  private static Logger logger = LoggerFactory.getLogger(SocketioClientFactory.class);

  private static AtomicLong onlineCount =new AtomicLong(0);

  private static ConcurrentHashMap<String,SocketioSession> clientMap = new ConcurrentHashMap<String,SocketioSession>();


  public static void addClient(SocketIOClient client, Long uid) {
    String sessionId = client.getSessionId().toString();
    SocketioSession socketioSession = new SocketioSession(client,uid);
    SocketioSession preSession =clientMap.remove(sessionId);
    if(preSession!=null && preSession.isChannelOpen()) {
      preSession.disconnect();
    }
    clientMap.put(sessionId,socketioSession);
    long curCnt= onlineCount.addAndGet(1);
    PtyRunnerFactory.createRunner(socketioSession);
    logger.info("SocketioClientFactory addClient,sessionId={},uid={}，curCnt={}",sessionId,uid,curCnt);
  }

  public static void closeClient(SocketIOClient client) {
    String sessionId =client.getSessionId().toString();
    SocketioSession socketioSession =clientMap.remove(sessionId);
    Long uid =null;
    if(socketioSession!=null) {
      uid = socketioSession.getUid();
      onlineCount.decrementAndGet();
    }
    PtyRunnerFactory.closeRunner(sessionId);
    long curCnt = getConnectCount();
    logger.info("SocketioClientFactory closeClient,sessionId={},uid={}，curCnt={}",sessionId,uid,curCnt);
  }

  public static Collection<SocketioSession> getAllClient() {
    return clientMap.values();
  }

  public static long getConnectCount() {
    return onlineCount.get();
  }

  public static SocketioSession getBySession(String  sessionId) {
    SocketioSession client = clientMap.get(sessionId);
    return client;
  }

  public static SocketioSession getByClient(SocketIOClient client) {
    String sessionId = client.getSessionId().toString();
    return clientMap.get(sessionId);
  }

  public static void closeAll() {
    logger.info("SocketioClientFactory closeAll start");
    PtyRunnerFactory.closeAll();
    for(Map.Entry<String,SocketioSession> entry: clientMap.entrySet()) {
      SocketioSession socketioSession = entry.getValue();
      socketioSession.getClient().disconnect();
    }
  }

}
