package com.socketio.core;

import com.corundumstudio.socketio.SocketIOClient;
import com.socketio.core.runner.IPtyRunner;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@EqualsAndHashCode
public class SocketioSession  implements Serializable {

  private static Logger logger= LoggerFactory.getLogger(SocketioSession.class);

  private String sessionId;

  private Long uid;

  private SocketIOClient client;

  private IPtyRunner ptyRunner;

  public SocketioSession() {

  }

  public SocketioSession(SocketIOClient client,Long uid) {
    this.client =client;
    this.sessionId = client.getSessionId().toString();
    this.uid= uid;
  }

  public void sendEventMessage(String event,String info){
    SocketioOperator.sendEventInfo(client,event,info);
  }

  public  boolean isChannelOpen() {
    return client.isChannelOpen();
  }

  public void disconnect() {
    client.disconnect();
  }
}