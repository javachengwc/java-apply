package com.socketio.core.runner;

import com.socketio.core.SocketioSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PtyRunnerFactory {

  private static Logger logger = LoggerFactory.getLogger(PtyRunnerFactory.class);

  private static ConcurrentHashMap<String, IPtyRunner> runnerMap = new ConcurrentHashMap<String,IPtyRunner>();

  public static IPtyRunner createRunner(SocketioSession socketioSession ) {
    String sessionId = socketioSession.getSessionId();
    Long uid = socketioSession.getUid();
    logger.info("PtyRunnerFactory createRunner start,uid={},sessionId={}",uid,sessionId);
    IPtyRunner ptyRunner = new PtyRunner(socketioSession);
    ptyRunner.init();
    addRunner(ptyRunner);
    return ptyRunner;
  }

  public static void addRunner(IPtyRunner ptyRunner) {
    Long uid = ptyRunner.getUid();
    String sessionId = ptyRunner.getSessionId();
    logger.info("PtyRunnerFactory addRunner start,uid={},sessionId={}",uid,sessionId);
    IPtyRunner oldRunner = runnerMap.remove(sessionId);
    if(oldRunner!=null ) {
      oldRunner.destroy();
    }
    runnerMap.put(sessionId,ptyRunner);
  }

  public static void closeRunner(String sessionId) {
    logger.info("PtyRunnerFactory closeRunner start,sessionId={}，",sessionId);
    IPtyRunner ptyRunner = getBySession(sessionId);
    if(ptyRunner!=null ) {
      ptyRunner.destroy();
      runnerMap.remove(sessionId);
    }
  }

  public static void closeAll() {
    logger.info("PtyRunnerFactory closeAll start");
    for(Map.Entry<String,IPtyRunner> entry: runnerMap.entrySet()) {
      IPtyRunner ptyRunner = entry.getValue();
      ptyRunner.destroy();
    }
  }

  public static IPtyRunner getBySession(String sessionId) {
    IPtyRunner ptyRunner = runnerMap.get(sessionId);
    return  ptyRunner;
  }
}
