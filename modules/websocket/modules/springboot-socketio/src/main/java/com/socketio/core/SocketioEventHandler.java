package com.socketio.core;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.socketio.core.runner.IPtyRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketioEventHandler {

  @Autowired
  private SocketIOServer socketIoServer;

  private static String TOKEN_PARAM ="token";

  //客户端连接的时候触发
  @OnConnect
  public void onConnect(SocketIOClient client) {
    String sessionId = client.getSessionId().toString();
    log.info("SocketioEventHandler onConnect start,sessionId={},",sessionId);
    String token = client.getHandshakeData().getSingleUrlParam(TOKEN_PARAM);
    String ip =client.getHandshakeData().getAddress().getAddress().getHostAddress();
    log.info("SocketioEventHandler onConnect sessionId={},token={},ip={}",sessionId,token,ip);

    Long uid = Long.parseLong(RandomStringUtils.randomNumeric(6));
    SocketioClientFactory.addClient(client,uid);
    log.info("SocketioEventHandler onConnect success,sessionId={},",uid,client.getSessionId());
  }

  //客户端关闭连接时触发
  @OnDisconnect
  public void onDisconnect(SocketIOClient client) {
    log.info("SocketioEventHandler onDisconnect start,sessionId={},",client.getSessionId());
    SocketioClientFactory.closeClient(client);
  }

  @OnEvent(value = SocketioEvent.UP_CODE)
  public void code(SocketIOClient client, String code) {
    log.info("SocketioEventHandler code start,sessionId={},code={}",client.getSessionId(),code);
    SocketioSession socketioSession = SocketioClientFactory.getByClient(client);
    if(socketioSession==null) {
      log.info("SocketioEventHandler code socketioSession is null,sessionId={}",client.getSessionId());
      client.sendEvent(SocketioEvent.DOWN_ERROR, "连接丢失，请重连");
      return;
    }
    IPtyRunner ptyRunner = socketioSession.getPtyRunner();
    ptyRunner.exec(code);
  }

  @OnEvent(value = SocketioEvent.UP_MSG)
  public void msg(SocketIOClient client, String param) {
    log.info("SocketioEventHandler msg start,sessionId={},param={}",client.getSessionId(),param);
    SocketioSession socketioSession = SocketioClientFactory.getByClient(client);
    if(socketioSession==null) {
      log.info("SocketioEventHandler msg socketioSession is null,sessionId={}",client.getSessionId());
      client.sendEvent(SocketioEvent.DOWN_ERROR, "连接丢失，请重连");
      return;
    }
    IPtyRunner ptyRunner = socketioSession.getPtyRunner();
    ptyRunner.input(param);
  }

  //调整窗口大小,非常重要，直接影响输出是否乱码和或多或少信息
  @OnEvent(value = SocketioEvent.UP_RESIZE)
  public void resize(SocketIOClient client, String size) {
    log.info("SocketioEventHandler resize start,sessionId={},size={}",client.getSessionId(),size);
    SocketioSession socketioSession = SocketioClientFactory.getByClient(client);
    if(socketioSession==null) {
      log.info("SocketioEventHandler resize socketioSession is null,sessionId={}",client.getSessionId());
      client.sendEvent(SocketioEvent.DOWN_ERROR, "连接丢失，请重连");
      return;
    }
    IPtyRunner ptyRunner = socketioSession.getPtyRunner();
    ptyRunner.resize(size);
  }
}